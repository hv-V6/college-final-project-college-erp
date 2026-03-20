package com.college.erp.service;

import com.college.erp.model.Timetable;
import com.college.erp.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> findAll() { return timetableRepository.findAll(); }

    public Timetable findById(Long id) {
        return timetableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timetable entry not found: " + id));
    }

    public void save(Timetable timetable) { timetableRepository.save(timetable); }

    public void deleteById(Long id) { timetableRepository.deleteById(id); }
}