package com.zup.e_commerce.mappers;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.models.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest customerRequest) {

        return new Customer(customerRequest.name(),
                customerRequest.cpf(),
                customerRequest.email());
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getEmail());
    }
}
