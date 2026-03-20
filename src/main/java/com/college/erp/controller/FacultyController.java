package com.college.erp.controller;

import com.college.erp.model.Faculty;
import com.college.erp.service.FacultyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("facultyList", facultyService.findAll());
        return "faculty/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("faculty", new Faculty());
        model.addAttribute("pageTitle", "Add Faculty");
        return "faculty/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("faculty", facultyService.findById(id));
        model.addAttribute("pageTitle", "Edit Faculty");
        return "faculty/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Faculty faculty) {
        facultyService.save(faculty);
        return "redirect:/faculty";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        facultyService.deleteById(id);
        return "redirect:/faculty";
    }
}