package com.college.erp.service;

import com.college.erp.model.Fee;
import com.college.erp.repository.FeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeService {

    private final FeeRepository feeRepository;

    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    public List<Fee> findAll() { return feeRepository.findAll(); }

    public Fee findById(Long id) {
        return feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found: " + id));
    }

    public void save(Fee fee) { feeRepository.save(fee); }

    public void deleteById(Long id) { feeRepository.deleteById(id); }
}