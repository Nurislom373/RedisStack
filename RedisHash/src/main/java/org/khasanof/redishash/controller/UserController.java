package org.khasanof.redishash.controller;

import org.khasanof.redishash.model.User;
import org.khasanof.redishash.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<User> all() {
        return userRepository.findAll();
    }

    @GetMapping("/{isbn}")
    public User get(@PathVariable String isbn) {
        return userRepository.findById(isbn).get();
    }
}
