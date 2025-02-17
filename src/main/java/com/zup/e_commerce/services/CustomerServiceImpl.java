package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.exceptions.CustomerNotFoundException;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.mappers.CustomerMapper;
import com.zup.e_commerce.models.Customer;
import com.zup.e_commerce.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

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

        Customer customer = customerMapper.toEntity(customerRequest);
        customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }
    @Override
    public List<CustomerResponse> getAllCustomers(){
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerByCpf(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com CPF: " + cpf + " não encontrado."));
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(String cpf, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com CPF: " + cpf + " não encontrado."));
        customer.setName(customerRequest.name());
        customer.setCpf(customerRequest.cpf());
        customer.setEmail(customerRequest.email());
        customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }


}
