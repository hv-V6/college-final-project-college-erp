package com.college.erp.service;

import com.college.erp.model.Exam;
import com.college.erp.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public Exam findById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found: " + id));
    }

    public void save(Exam exam) {
        examRepository.save(exam);
    }

    public void deleteById(Long id) {
        examRepository.deleteById(id);
    }
}