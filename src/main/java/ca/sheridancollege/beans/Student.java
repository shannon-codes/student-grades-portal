package ca.sheridancollege.beans;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Student  {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotEmpty(message="cannot be empty")
	@Size(min=2, max=200, message="Name must be between 2 and 200 characters")
	private String name;
	
	@NotEmpty(message="cannot be empty")
	@Size(min=2, max=200, message="Student ID must be between 2 and 200 characters")
	private String studentId;

	@NotNull(message="Exercises grade cannot be empty, put 0 if undetermined")
	@Min(value=0,message="Grade must be between 0-100")
	@Max(value=100,message="Grade must be between 0-100")
	private Double exercises;
	
	@NotNull(message="Assignment 1 grade cannot be empty, put 0 if undetermined")
	@Min(value=0,message="Grade must be between 0-100")
	@Max(value=100,message="Grade must be between 0-100")
	private Double assignment1;
	
	@NotNull(message="Assignment 2 grade cannot be empty, put 0 if undetermined")
	@Min(value=0,message="Grade must be between 0-100")
	@Max(value=100,message="Grade must be between 0-100")
	private Double assignment2;
	
	@NotNull(message="Midterm grade cannot be empty, put 0 if undetermined")
	@Min(value=0,message="Grade must be between 0-100")
	@Max(value=100,message="Grade must be between 0-100")
	private Double midterm;
	
	@NotNull(message="Final Exam grade cannot be empty, put 0 if undetermined")
	@Min(value=0,message="Grade must be between 0-100")
	@Max(value=100,message="Grade must be between 0-100")
	private Double finalExam;
	
	@NotNull(message="Final Project grade cannot be empty, put 0 if undetermined")
	@Min(value=0,message="Grade must be between 0-100")
	@Max(value=100,message="Grade must be between 0-100")
	private Double finalProject;

	public Student(String name, String studentId, double exercises, double assignment1, double assignment2,
			double midterm, double finalExam, double finalProject) {
		super();
		this.name = name;
		this.studentId = studentId;
		this.exercises = exercises;
		this.assignment1 = assignment1;
		this.assignment2 = assignment2;
		this.midterm = midterm;
		this.finalExam = finalExam;
		this.finalProject = finalProject;
	}
	
	public double calculateWeightedAvg() {
		double weightedAvg = (exercises/100.0)*10+
				(assignment1/100.0)*5+
				(assignment2/100.0)*5+
				(midterm/100.0)*30+
				(finalExam/100.0)*35+
				(finalProject/100.0)*15;
		return weightedAvg;
	}
	
	public String getFormattedWeightedAvg() {
		
		DecimalFormat df = new DecimalFormat("#.#"); 
		return df.format(calculateWeightedAvg());
	}
	
	public String calculateLetterGrade() {
		
		double dWeightedAvg = calculateWeightedAvg();
		
		if(dWeightedAvg >=90) {
			return "A+";
		}else if(dWeightedAvg>=80) {
			return "A";
		}else if(dWeightedAvg>=75) {
			return "B+";
		}else if(dWeightedAvg>=70) {
			return "B";
		}else if(dWeightedAvg>=65) {
			return "C+";
		}else if(dWeightedAvg>=60) {
			return "C";
		}else if(dWeightedAvg>=50) {
			return "D";
		}else {
			return "F";
		}
		
	}
	
	
}
