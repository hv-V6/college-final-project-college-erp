package com.college.erp.controller;

import com.college.erp.service.FacultyService;
import com.college.erp.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	private final StudentService studentService;
	private final FacultyService facultyService;

	public DashboardController(StudentService studentService, FacultyService facultyService) {
		this.studentService = studentService;
		this.facultyService = facultyService;
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("totalStudents", studentService.findAll().size());
		model.addAttribute("totalFaculty", facultyService.findAll().size());
		return "dashboard";
	}
}