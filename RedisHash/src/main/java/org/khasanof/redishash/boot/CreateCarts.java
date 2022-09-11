package org.khasanof.redishash.boot;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.Cart;
import org.khasanof.redishash.model.CartItem;
import org.khasanof.redishash.model.User;
import org.khasanof.redishash.repository.BookRepository;
import org.khasanof.redishash.repository.CartRepository;
import org.khasanof.redishash.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

@Component
@Order(5)
@Slf4j
public class CreateCarts implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CartService cartService;

    @Value("${app.numberOfCarts}")
    private Integer numberOfCarts;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        IntStream.range(0, numberOfCarts).forEach(n -> {
            String userId = redisTemplate.opsForSet()
                    .randomMember(User.class.getName());

            Cart cart = Cart.builder()
                    .userId(userId)
                    .build();

            Set<Book> books = getRandomBooks(bookRepository, 7);

            cart.setCartItems(getCartItemsForBooks(books));

            cartRepository.save(cart);

            if (random.nextBoolean()) {
                cartService.checkout(cart.getId());
            }
        });

        log.info(">>>>>>>>> Created Carts..................");
    }

    private Set<Book> getRandomBooks(BookRepository bookRepository, int max) {
        Random random = new Random();
        int howMany = random.nextInt(max) + 1;
        Set<Book> books = new HashSet<>();
        IntStream.range(0, howMany).forEach(num -> {
            String bookId = redisTemplate.opsForSet().randomMember(Book.class.getName());
            books.add(bookRepository.findById(bookId).get());
        });

        return books;
    }

    private Set<CartItem> getCartItemsForBooks(Set<Book> books) {
        Set<CartItem> items = new HashSet<CartItem>();
        books.forEach(book -> {
            CartItem item = CartItem.builder()//
                    .isbn(book.getId()) //
                    .price(book.getPrice()) //
                    .quantity(1L) //
                    .build();
            items.add(item);
        });

        return items;
    }
}
