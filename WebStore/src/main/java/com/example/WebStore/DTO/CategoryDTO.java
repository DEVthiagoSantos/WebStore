package com.example.WebStore.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDTO(@NotNull Long id, @NotBlank String name) {
}
