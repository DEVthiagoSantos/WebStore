package com.example.WebStore.DTO;

import jakarta.validation.constraints.Min;

public record CartUpdateItemDTO(
        @Min(1)
        int quantity
) {
}
