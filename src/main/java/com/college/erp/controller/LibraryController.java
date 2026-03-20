package com.college.erp.controller;

import com.college.erp.model.LibraryBook;
import com.college.erp.service.LibraryBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/library")
public class LibraryController {

    private final LibraryBookService libraryBookService;

    public LibraryController(LibraryBookService libraryBookService) {
        this.libraryBookService = libraryBookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", libraryBookService.findAll());
        return "library/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("book", new LibraryBook());
        model.addAttribute("bookStatuses", LibraryBook.BookStatus.values());
        return "library/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", libraryBookService.findById(id));
        model.addAttribute("bookStatuses", LibraryBook.BookStatus.values());
        return "library/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute LibraryBook book) {
        libraryBookService.save(book);
        return "redirect:/library";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        libraryBookService.deleteById(id);
        return "redirect:/library";
    }
}