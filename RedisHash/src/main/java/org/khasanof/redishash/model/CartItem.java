package org.khasanof.redishash.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItem {
    private String isbn;
    private Double price;
    private Long quantity;
}
