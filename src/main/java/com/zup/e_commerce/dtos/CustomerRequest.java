package com.zup.e_commerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerRequest(
        @NotBlank(message = "O nome do cliente é obrigatório.")
        String name,


        @CPF(message = "O CPF deve ser válido.")
        String cpf,


        @Email(message = "O email deve ser válido.")
        String email
) {
}
