package ca.sheridancollege.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.sheridancollege.beans.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
Student findByName(String name);
List<Student> findAll();
Student findById(long id);
}
