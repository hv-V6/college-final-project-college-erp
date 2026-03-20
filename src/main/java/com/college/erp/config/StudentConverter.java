package com.college.erp.config;

import com.college.erp.service.StudentService;
import com.college.erp.model.Student;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter implements Converter<String, Student> {

    private final StudentService studentService;

    public StudentConverter(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Student convert(String id) {
        if (id == null || id.isBlank()) return null;
        return studentService.findById(Long.parseLong(id));
    }
}