package org.khasanof.redisserver.data.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@RedisHash
public class Role {
    @Id
    private String id;
    private String name;
}
