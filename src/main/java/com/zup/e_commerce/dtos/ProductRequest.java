package com.zup.e_commerce.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "o nome do produto é obrigatório") String name,
        @Positive(message = "O preço deve ser maior que zero") double price,
        @Min(value = 0, message = "A quantidade deve ser maior ou igual a zero") int quantity) {
}
