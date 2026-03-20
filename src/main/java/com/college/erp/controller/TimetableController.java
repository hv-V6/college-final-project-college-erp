package com.college.erp.controller;

import com.college.erp.model.Timetable;
import com.college.erp.service.TimetableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    private static final List<String> DAYS = List.of(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    );

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("entries", timetableService.findAll());
        return "timetable/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("entry", new Timetable());
        model.addAttribute("days", DAYS);
        return "timetable/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("entry", timetableService.findById(id));
        model.addAttribute("days", DAYS);
        return "timetable/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Timetable entry) {
        timetableService.save(entry);
        return "redirect:/timetable";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        timetableService.deleteById(id);
        return "redirect:/timetable";
    }
}