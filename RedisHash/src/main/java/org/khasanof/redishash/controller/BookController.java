package org.khasanof.redishash.controller;

import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.Category;
import org.khasanof.redishash.repository.BookRepository;
import org.khasanof.redishash.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public Iterable<Book> all() {
        return bookRepository.findAll();
    }

    @GetMapping("/categories")
    public Iterable<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{isbn}")
    public Book get(@PathVariable("isbn") String isbn) {
        return bookRepository.findById(isbn).get();
    }
}
