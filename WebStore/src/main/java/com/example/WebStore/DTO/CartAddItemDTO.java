package com.example.WebStore.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartAddItemDTO(

        @NotNull
        Long productId,

        @Min(1)
        int quantity
) {
}
