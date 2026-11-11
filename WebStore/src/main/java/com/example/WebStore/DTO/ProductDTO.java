package com.example.WebStore.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDTO(@NotNull Long id, @NotBlank String name,
                         @NotBlank String description,
                         @NotNull BigDecimal price,
                         @NotBlank String image,
                         @NotNull Long category_id,
                         @NotBlank String category_name) {
}
