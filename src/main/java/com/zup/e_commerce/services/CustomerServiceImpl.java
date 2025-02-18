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
        validateInputCustomerRequestNotNull(customerRequest);
        validateUniqueFields(customerRequest);

        Customer customer = customerMapper.toEntity(customerRequest);
        customerRepository.save(customer);

        return customerMapper.toResponse(customer);
    }


    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerByCpf(String cpf) {
        validateInputCpfNotNull(cpf);

        Customer customer = findCustomerByCpf(cpf);

        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(String cpf, CustomerRequest customerRequest) {
        validateInputCpfNotNull(cpf);

        validateInputCustomerRequestNotNull(customerRequest);

        Customer customer = findCustomerByCpf(cpf);

        updateCustomerData(customer, customerRequest);

        customerRepository.save(customer);

        return customerMapper.toResponse(customer);
    }

    private void validateInputCpfNotNull(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }
    }

    private void validateInputCustomerRequestNotNull(CustomerRequest customerRequest) {
        if (customerRequest == null) {
            throw new IllegalArgumentException("CustomerRequest não pode ser nulo.");
        }
    }

    private Customer findCustomerByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com CPF: " + cpf + " não encontrado."));
    }

    private void updateCustomerData(Customer customer, CustomerRequest customerRequest) {
        customer.setName(customerRequest.name());
        customer.setEmail(customerRequest.email());
    }

    private void validateUniqueFields(CustomerRequest customerRequest) {
        validateUniqueCpf(customerRequest.cpf());
        validateUniqueEmail(customerRequest.email());
    }

    private void validateUniqueCpf(String cpf) {
        if (customerRepository.existsByCpf(cpf)) {
            throw new DuplicateFieldException("Já existe um cliente cadastrado com o CPF: " + cpf + ".");
        }
    }

    private void validateUniqueEmail(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateFieldException("Já existe um cliente cadastrado com o email: " + email + ".");
        }
    }

}
