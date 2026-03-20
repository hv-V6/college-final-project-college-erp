package com.college.erp.service;

import com.college.erp.model.Student;
import com.college.erp.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() { return studentRepository.findAll(); }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
    }

    public void save(Student student) { studentRepository.save(student); }

    public void deleteById(Long id) { studentRepository.deleteById(id); }
}