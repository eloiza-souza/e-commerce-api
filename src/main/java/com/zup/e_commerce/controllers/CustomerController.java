package com.zup.e_commerce.controllers;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.services.CustomerService;
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

@RestController
@RequestMapping("/clientes")
@Validated
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(customerRequest));
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<CustomerResponse> getConsumerByCpf(
            @PathVariable("cpf") @CPF (message = "CPF inválido") String cpf){
        return ResponseEntity.ok(customerService.getCustomerByCpf(cpf));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<CustomerResponse> updateProduct(
            @PathVariable("cpf") @CPF (message = "CPF inválido") String cpf,
            @Valid @RequestBody CustomerRequest customerRequest){
        return ResponseEntity.ok(customerService.updateCustomer(cpf, customerRequest));
    }


}
