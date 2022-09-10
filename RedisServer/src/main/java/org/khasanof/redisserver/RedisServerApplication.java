package org.khasanof.redisserver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class RedisServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisServerApplication.class, args);
    }

}
