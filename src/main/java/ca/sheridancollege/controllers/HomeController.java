package ca.sheridancollege.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.beans.Student;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.repositories.*;

@Controller
public class HomeController{

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	@Lazy
	UserRepository userRepo;

	@GetMapping("/")
	public String goHome() {
		return "index.html";
	}

	@GetMapping("/login")
	public String login() {
		return "login.html";
	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "error/access-denied.html";
	}

	@GetMapping("/student")
	public String goStudentHome(Authentication auth, Model model) {
		String studentName = auth.getName();
		Student student = studentRepo.findByName(studentName);
		model.addAttribute("student", student);
		return "student/index.html";
	}

	@GetMapping("/professor")
	public String goProfessorHome(Model model) {

		List<Student> students = studentRepo.findAll();

		model.addAttribute("overall", calculateOverallClassAvg(students));
		model.addAttribute("students", students);
		model.addAttribute("avgsByCategory", calculateGradeCategoryAvg(students));

		return "professor/index.html";
	}

	public String calculateOverallClassAvg(List<Student> students) {

		DecimalFormat df = new DecimalFormat("#.#");

		double sum = 0.0;

		String overall = "0.0";

		if (students.size() > 0) {
			for (Student s : students) {
				sum += s.calculateWeightedAvg();
			}

			overall = df.format(sum / students.size());
		}

		return overall;
	}

	public String[] calculateGradeCategoryAvg(List<Student> students) {

		double[] gradesArr = new double[6];
		String[] stringGradesArr = new String[6];
		DecimalFormat df = new DecimalFormat("#.#");

		int numberOfStudents = students.size();

		if (numberOfStudents > 0) {

			for (int i = 0; i < numberOfStudents; i++) {

				gradesArr[0] += students.get(i).getExercises();
				gradesArr[1] += students.get(i).getAssignment1();
				gradesArr[2] += students.get(i).getAssignment2();
				gradesArr[3] += students.get(i).getMidterm();
				gradesArr[4] += students.get(i).getFinalExam();
				gradesArr[5] += students.get(i).getFinalProject();
			}

			for (int i = 0; i < gradesArr.length; i++) {

				stringGradesArr[i] = df.format(gradesArr[i] / numberOfStudents);

			}
		}
		return stringGradesArr;
	}

	@GetMapping("/createStudent")
	public String goCreateStudent(Model model) {

		Student student = new Student();

		model.addAttribute("student", student);
		return "professor/createStudent.html";
	}

	@PostMapping("/createStudent")
	public String doCreateStudent(@ModelAttribute Student student, Model model) {

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Student>> validationErrors = validator.validate(student);

		if (!validationErrors.isEmpty()) {
			List<String> myListErrors = new ArrayList<String>();
			for (ConstraintViolation<Student> error : validationErrors) {
				myListErrors.add(error.getPropertyPath().toString().toUpperCase() + " :: " + error.getMessage());
			}
			model.addAttribute("errorMessage", myListErrors);
			return "professor/createStudent.html";
		} else {
			User user = new User(student.getName(), encodePassword(student.getStudentId()));
			user.getRoles().add(roleRepo.findByRolename("ROLE_STUDENT"));
			studentRepo.save(student);
			userRepo.save(user);
			return "redirect:/createStudent";
		}

	}

	@GetMapping("/edit/{id}")
	public String goEdit(@PathVariable long id, Model model) {
		if (studentRepo.findById(id) != null) {
			Student student = studentRepo.findById(id);
			model.addAttribute("student", student);
			return "professor/editStudent.html";
		} else {
			return "redirect:/professor";
		}
	}

	@PostMapping("saveEditStudent")
	public String saveEditStudent(@ModelAttribute Student student, Model model) {

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Student>> validationErrors = validator.validate(student);

		if (!validationErrors.isEmpty()) {
			List<String> myListErrors = new ArrayList<String>();
			for (ConstraintViolation<Student> error : validationErrors) {
				myListErrors.add(error.getPropertyPath().toString().toUpperCase() + " :: " + error.getMessage());
			}
			model.addAttribute("errorMessage", myListErrors);
			return "professor/editStudent.html";
		} else {

			studentRepo.save(student);
			return "redirect:/professor";
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable long id) {
		if (studentRepo.findById(id) != null) {
			studentRepo.deleteById(id);
		}
		return "redirect:/professor";
	}

	private String encodePassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}

}
