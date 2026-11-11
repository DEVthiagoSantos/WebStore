package com.example.WebStore.DTO;

import java.math.BigDecimal;

public record CartItemDTO(
        Long productId,
        String name,
        String image,
        BigDecimal price,
        int quantity,
        BigDecimal subtotal
) {
}
