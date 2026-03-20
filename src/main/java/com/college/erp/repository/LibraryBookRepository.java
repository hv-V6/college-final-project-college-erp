package com.college.erp.repository;

import com.college.erp.model.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
}