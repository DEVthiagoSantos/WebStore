package com.example.WebStore.DTO;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDTO(
        Long cartId,
        List<CartItemDTO> items,
        BigDecimal total
) {
}
