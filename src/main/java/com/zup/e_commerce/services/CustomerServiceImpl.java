package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.exceptions.CustomerNotFoundException;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.models.Customer;
import com.zup.e_commerce.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerResponse addCustomer(CustomerRequest customerRequest) {
        String cpf = customerRequest.cpf();
        if (customerRepository.existsByCpf(cpf)) {
            throw new DuplicateFieldException("Já existe um cliente cadastrado com o CPF: " + cpf + ".");
        }

        String email = customerRequest.email();
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateFieldException("Já existe um cliente cadastrado com o email: " + email + ".");
        }

        Customer customer = mapToEntity(customerRequest);
        customerRepository.save(customer);
        return mapToResponse(customer);
    }
    @Override
    public List<CustomerResponse> getAllCustomers(){
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerByCpf(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com CPF: " + cpf + " não encontrado."));
        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(String cpf, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com CPF: " + cpf + " não encontrado."));
        customer.setName(customerRequest.name());
        customer.setCpf(customerRequest.cpf());
        customer.setEmail(customerRequest.email());
        customerRepository.save(customer);
        return mapToResponse(customer);
    }

    private Customer mapToEntity(CustomerRequest customerRequest) {

        return new Customer(customerRequest.name(),
                customerRequest.cpf(),
                customerRequest.email());
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return new CustomerResponse(customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getEmail());
    }
}
