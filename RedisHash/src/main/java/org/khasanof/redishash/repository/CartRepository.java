package org.khasanof.redishash.repository;

import com.redislabs.modules.rejson.JReJSON;
import org.khasanof.redishash.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class CartRepository implements CrudRepository<Cart, String> {

    private JReJSON redisJson = new JReJSON();
    private final static String idPrefix = Cart.class.getName();

    @Autowired
    private RedisTemplate<String, String> template;

    private SetOperations<String, String> redisSets() {
        return template.opsForSet();
    }

    private HashOperations<String, String, String> redisHash() {
        return template.opsForHash();
    }

    @Override
    public <S extends Cart> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        String key = getKey(entity);
        redisJson.set(key, entity);
        redisSets().add(idPrefix, key);
        redisHash().put("carts-by-user-id-idx", entity.getUserId().toString(), entity.getId().toString());

        return entity;
    }

    @Override
    public <S extends Cart> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cart> findById(String s) {
        Cart cart = redisJson.get(getKey(s), Cart.class);
        return Optional.ofNullable(cart);
    }

    @Override
    public boolean existsById(String s) {
        return template.hasKey(getKey(s));
    }

    @Override
    public Iterable<Cart> findAll() {
        String[] keys = redisSets().members(idPrefix).stream().toArray(String[]::new);
        return redisJson.mget(Cart.class, keys);
    }

    @Override
    public Iterable<Cart> findAllById(Iterable<String> strings) {
        String[] array = StreamSupport.stream(strings.spliterator(), false)
                .map(id -> getKey(id)).toArray(String[]::new);
        return redisJson.mget(Cart.class, array);
    }

    @Override
    public long count() {
        return redisSets().size(idPrefix);
    }

    @Override
    public void deleteById(String s) {
        redisJson.del(getKey(s));
    }

    @Override
    public void delete(Cart entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        List<String> keys = StreamSupport.stream(strings.spliterator(), false)
                .map(id -> idPrefix + id)
                .collect(Collectors.toList());
        redisSets().getOperations().delete(keys);
    }

    @Override
    public void deleteAll(Iterable<? extends Cart> entities) {
        List<String> keys = StreamSupport.stream(entities.spliterator(), false)
                .map(cart -> idPrefix + cart.getId())
                .collect(Collectors.toList());
        redisSets().getOperations().delete(keys);
    }

    @Override
    public void deleteAll() {
        redisSets().getOperations().delete(redisSets().members(idPrefix));
    }

    public Optional<Cart> findByUserId(Long id) {
        String cartId = redisHash().get("carts-by-user-id-idx", id.toString());
        return (cartId != null) ? findById(cartId) : Optional.empty();
    }

    public static String getKey(Cart cart) {
        return String.format("%s:%s", idPrefix, cart.getId());
    }

    public static String getKey(String id) {
        return String.format("%s:%s", idPrefix, id);
    }
}
