package com.college.erp.controller;

import com.college.erp.model.Attendance;
import com.college.erp.service.AttendanceService;
import com.college.erp.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    public AttendanceController(AttendanceService attendanceService, StudentService studentService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("attendanceList", attendanceService.findAll());
        return "attendance/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("students", studentService.findAll());
        return "attendance/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("attendance", attendanceService.findById(id));
        model.addAttribute("students", studentService.findAll());
        return "attendance/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Attendance attendance) {
        attendanceService.save(attendance);
        return "redirect:/attendance";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        attendanceService.deleteById(id);
        return "redirect:/attendance";
    }
}