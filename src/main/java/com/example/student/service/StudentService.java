package com.example.student.service;

import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // 🔹 Create a student and clear the "students_cache" so it fetches fresh data next time
    @CacheEvict(value = "students_cache", allEntries = true)
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // 🔹 Get all students, cache the result
    @Cacheable(value = "students_cache")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 🔹 Get student by ID, cache the result
    @Cacheable(value = "student_cache", key = "#id")
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // 🔹 Update student, update cache for the student and clear all students list cache
    @CachePut(value = "student_cache", key = "#id")
    @CacheEvict(value = "students_cache", allEntries = true)
    public Student updateStudent(Long id, Student updateStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setFirstName(updateStudent.getFirstName());
            student.setLastName(updateStudent.getLastName());
            student.setEmail(updateStudent.getEmail());
            student.setPhone(updateStudent.getPhone());
            student.setCollege(updateStudent.getCollege());
            return studentRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // 🔹 Delete student, remove from cache
    @CacheEvict(value = "student_cache", key = "#id" , allEntries = true)
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
