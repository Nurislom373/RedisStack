package org.khasanof.redishash.boot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.Category;
import org.khasanof.redishash.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Order(1)
@Slf4j
public class CreateCategory implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        categoryRepository.deleteAll();

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Category>> typeReference = new TypeReference<>() {
        };

        InputStream inputStream = getClass().getResourceAsStream("/data/redis_category.json");

        List<Category> categories = objectMapper.readValue(inputStream, typeReference);

        categoryRepository.saveAll(categories);
        log.info(">>>> " + categories.size() + " Categories Saved!");
    }
}
