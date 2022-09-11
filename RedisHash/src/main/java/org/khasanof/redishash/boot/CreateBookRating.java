package org.khasanof.redishash.boot;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.BookRating;
import org.khasanof.redishash.model.User;
import org.khasanof.redishash.repository.BookRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.IntStream;

@Component
@Order(4)
@Slf4j
public class CreateBookRating implements CommandLineRunner {

    @Value("${app.numberOfRatings}")
    private Integer numberOfRatings;

    @Value("${app.ratingStars}")
    private Integer ratingStars;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private BookRatingRepository bookRatingRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        IntStream.range(0, numberOfRatings).forEach(n -> {
            String bookId = redisTemplate.opsForSet().randomMember(Book.class.getName());
            String userId = redisTemplate.opsForSet().randomMember(User.class.getName());

            int stars = random.nextInt(ratingStars) + 1;

            User user = new User();
            user.setId(userId);

            Book book = new Book();
            book.setId(bookId);

            BookRating rating = BookRating.builder()
                    .user(user)
                    .book(book)
                    .rating(stars).build();
            bookRatingRepository.save(rating);
        });
    }
}
