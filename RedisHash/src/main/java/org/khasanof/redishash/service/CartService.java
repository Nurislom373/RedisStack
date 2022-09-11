package org.khasanof.redishash.service;

import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import org.khasanof.redishash.model.Book;
import org.khasanof.redishash.model.Cart;
import org.khasanof.redishash.model.CartItem;
import org.khasanof.redishash.model.User;
import org.khasanof.redishash.repository.BookRepository;
import org.khasanof.redishash.repository.CartRepository;
import org.khasanof.redishash.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.LongStream;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private JReJSON redisJson = new JReJSON();

    Path path = Path.of(".cartItems");

    public Cart get(String id) {
        return cartRepository.findById(id).get();
    }

    public void addToCart(String id, CartItem item) {
        Optional<Book> optional = bookRepository.findById(item.getIsbn());
        if (optional.isPresent()) {
            String cartKey = CartRepository.getKey(id);
            item.setPrice(optional.get().getPrice());
            redisJson.arrAppend(cartKey, path, item);
        }
    }

    public void removeFromCart(String id, String isbn) {
        Optional<Cart> optional = cartRepository.findById(id);
        if (optional.isPresent()) {
            Cart cart = optional.get();
            String key = CartRepository.getKey(cart.getId());
            ArrayList<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
            OptionalLong optionalLong = LongStream.range(0, cartItems.size())
                    .filter(i -> cartItems.get((int) i).getIsbn().equals(isbn)).findFirst();
            if (optionalLong.isPresent()) {
                redisJson.arrPop(key, CartItem.class, path, optionalLong.getAsLong());
            }
        }
    }

    public void checkout(String id) {
        Cart cart = cartRepository.findById(id).get();
        User user = userRepository.findById(cart.getUserId()).get();
        cart.getCartItems().forEach(cartItem -> {
            Book book = bookRepository.findById(cartItem.getIsbn()).get();
            user.addBook(book);
        });
        userRepository.save(user);
    }


}
