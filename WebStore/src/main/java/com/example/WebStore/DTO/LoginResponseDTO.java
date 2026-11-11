package com.example.WebStore.DTO;

public record LoginResponseDTO(
        String token,
        UserResponseDTO user) {
}
