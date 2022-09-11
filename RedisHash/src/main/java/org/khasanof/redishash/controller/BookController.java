package org.khasanof.redishash.controller;

import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.Category;
import org.khasanof.redishash.repository.BookRepository;
import org.khasanof.redishash.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/findAll")
    public Iterable<Book> all() {
        return bookRepository.findAll();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> all(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageRequest of = PageRequest.of(page, size);
        Page<Book> all = bookRepository.findAll(of);
        List<Book> books = all.hasContent() ? all.getContent() : Collections.emptyList();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("page", all.getNumber());
        response.put("pages", all.getTotalPages());
        response.put("total", all.getTotalElements());

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
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
