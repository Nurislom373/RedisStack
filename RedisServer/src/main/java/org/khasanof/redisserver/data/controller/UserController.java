package org.khasanof.redisserver.data.controller;

import org.khasanof.redisserver.data.entity.User;
import org.khasanof.redisserver.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    private Iterable<User> all() {
        return userRepository.findAll();
    }
}
