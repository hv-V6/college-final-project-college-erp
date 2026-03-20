package com.college.erp.service;

import com.college.erp.model.LibraryBook;
import com.college.erp.repository.LibraryBookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryBookService {

    private final LibraryBookRepository libraryBookRepository;

    public LibraryBookService(LibraryBookRepository libraryBookRepository) {
        this.libraryBookRepository = libraryBookRepository;
    }

    public List<LibraryBook> findAll() { return libraryBookRepository.findAll(); }

    public LibraryBook findById(Long id) {
        return libraryBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public void save(LibraryBook book) { libraryBookRepository.save(book); }

    public void deleteById(Long id) { libraryBookRepository.deleteById(id); }
}