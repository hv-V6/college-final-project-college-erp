package com.college.erp.controller;

import com.college.erp.model.Fee;
import com.college.erp.service.FeeService;
import com.college.erp.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fees")
public class FeeController {

    private final FeeService feeService;
    private final StudentService studentService;

    public FeeController(FeeService feeService, StudentService studentService) {
        this.feeService = feeService;
        this.studentService = studentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("fees", feeService.findAll());
        return "fees/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("fee", new Fee());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("feeStatuses", Fee.FeeStatus.values());
        return "fees/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("fee", feeService.findById(id));
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("feeStatuses", Fee.FeeStatus.values());
        return "fees/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Fee fee) {
        feeService.save(fee);
        return "redirect:/fees";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        feeService.deleteById(id);
        return "redirect:/fees";
    }
}