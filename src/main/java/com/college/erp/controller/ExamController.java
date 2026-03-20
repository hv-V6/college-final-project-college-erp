package com.college.erp.controller;

import com.college.erp.model.Exam;
import com.college.erp.service.ExamService;
import com.college.erp.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;
    private final StudentService studentService;

    public ExamController(ExamService examService, StudentService studentService) {
        this.examService = examService;
        this.studentService = studentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("examList", examService.findAll());
        return "exams/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("exam", new Exam());
        model.addAttribute("students", studentService.findAll());
        return "exams/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("exam", examService.findById(id));
        model.addAttribute("students", studentService.findAll());
        return "exams/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Exam exam) {
        examService.save(exam);
        return "redirect:/exams";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        examService.deleteById(id);
        return "redirect:/exams";
    }
}