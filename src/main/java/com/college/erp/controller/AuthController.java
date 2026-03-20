package com.college.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String login() {
		return "login"; // just renders login.html, NO redirect
	}
}