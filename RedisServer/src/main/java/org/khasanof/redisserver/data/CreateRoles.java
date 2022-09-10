package org.khasanof.redisserver.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.redisserver.data.entity.Role;
import org.khasanof.redisserver.data.entity.User;
import org.khasanof.redisserver.data.repository.RoleRepository;
import org.khasanof.redisserver.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Order(1)
@Slf4j
public class CreateRoles implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        Role admin = Role.builder().name("admin").build();
        Role customer = Role.builder().name("customer").build();
        roleRepository.save(admin);
        roleRepository.save(customer);

        ObjectMapper objectMapper = new ObjectMapper();

        TypeReference<List<User>> typeReference = new TypeReference<>() {};

        InputStream inputStream = getClass().getResourceAsStream("/users.json");

        List<User> users = objectMapper.readValue(inputStream, typeReference);

        users.forEach((user) -> {
            user.addRole(customer);
            userRepository.save(user);
        });
        log.info(">>>> " + users.size() + " Users Saved!");

        User adminUser = new User();
        adminUser.setName("Adminus Admistradore");
        adminUser.setEmail("admin@example.com");
        adminUser.addRole(admin);
        adminUser.setPassword("123");
        userRepository.save(adminUser);
        log.info(">>>> Loaded User Data and Created users...");
    }

}
