package com.zup.e_commerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PurchaseRequest (@NotBlank (message = "É preciso informar o CPF para realização da compra.")String cpf,
                               @NotEmpty(message = "É preciso informar pelo menos um produto para efetuar a compra.") List<String> productNames){

}
