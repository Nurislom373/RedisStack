package org.khasanof.redishash.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@RedisHash
public class BookRating {
    @Id
    private String id;

    @NotNull
    @Reference
    private User user;

    @NotNull
    @Reference
    private Book book;

    @NotNull
    private Integer rating;
}
