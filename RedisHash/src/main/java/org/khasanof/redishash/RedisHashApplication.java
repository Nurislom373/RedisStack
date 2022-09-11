package org.khasanof.redishash;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class RedisHashApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisHashApplication.class, args);
    }

}
