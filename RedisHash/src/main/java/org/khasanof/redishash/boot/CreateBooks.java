package org.khasanof.redishash.boot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.Category;
import org.khasanof.redishash.repository.BookRepository;
import org.khasanof.redishash.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
@Order(2)
@Slf4j
public class CreateBooks implements CommandLineRunner {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        bookRepository.deleteAll();

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Book>> typeReference = new TypeReference<>() {
        };

        List<Category> categories = StreamSupport.stream(categoryRepository.findAll().spliterator(), false).toList();

        InputStream inputStream = getClass().getResourceAsStream("/data/redis_book.json");

        List<Book> books = objectMapper.readValue(inputStream, typeReference);

        books.forEach((book -> {
            book.addCategory(categories.get(randomNum(0, categories.size())));
            book.addCategory(categories.get(randomNum(0, categories.size())));
            bookRepository.save(book);
        }));

        bookRepository.saveAll(books);
        log.info(">>>> " + books.size() + " Books Saved!");
    }

    private int randomNum(int var1, int var2) {
        return var1 + (int) (Math.random() * var2);
    }
}
