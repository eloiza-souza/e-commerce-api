package com.zup.e_commerce.controllers;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Validated
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    @Operation(summary = "Registrar um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos")
    })
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(customerRequest));
    }

    @Operation(summary = "Obter todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Obter cliente pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Formato de CPF inválido"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<CustomerResponse> getConsumerByCpf(
            @PathVariable("cpf") @CPF (message = "CPF inválido") String cpf){
        return ResponseEntity.ok(customerService.getCustomerByCpf(cpf));
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar cliente pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição ou formato de CPF inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<CustomerResponse> updateProduct(
            @PathVariable("cpf") @CPF (message = "CPF inválido") String cpf,
            @Valid @RequestBody CustomerRequest customerRequest){
        return ResponseEntity.ok(customerService.updateCustomer(cpf, customerRequest));
    }
}