package com.example.WebStore.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(@NotBlank String user_name,
                      @NotBlank @Email String email,
                      @NotBlank String senha) {
}
