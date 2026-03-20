package com.college.erp.service;

import com.college.erp.model.Faculty;
import com.college.erp.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty findById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found: " + id));
    }

    public void save(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public void deleteById(Long id) {
        facultyRepository.deleteById(id);
    }

    public long count() {
        return facultyRepository.count();
    }
}