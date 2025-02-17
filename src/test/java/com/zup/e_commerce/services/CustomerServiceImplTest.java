package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.mappers.CustomerMapper;
import com.zup.e_commerce.models.Customer;
import com.zup.e_commerce.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenAddCustomerWithValidRequest_shouldReturnCustomerResponse() {
        // given: a valid customer request
        CustomerRequest customerRequest = new CustomerRequest("Name Test", "12345678909", "test@example.com");
        Customer customer = new Customer("Name Test", "12345678909", "test@example.com");
        CustomerResponse customerResponse = new CustomerResponse(1L, "Name Test", "12345678909", "test@example.com");

        when(customerRepository.existsByCpf(customerRequest.cpf())).thenReturn(false);
        when(customerRepository.existsByEmail(customerRequest.email())).thenReturn(false);
        when(customerMapper.toEntity(customerRequest)).thenReturn(customer);
        when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

        // when: the addCustomer method is called
        CustomerResponse result = customerService.addCustomer(customerRequest);

        // then: the correct customer response is returned
        assertNotNull(result);
        assertEquals(customerResponse, result);
        verify(customerRepository).save(customer);
    }

    @Test
    void whenAddCustomerWithExistingCpf_shouldThrowDuplicateFieldException() {
        // given: a customer request with an existing CPF
        CustomerRequest customerRequest = new CustomerRequest("Name Test", "12345678909", "test@example.com");

        when(customerRepository.existsByCpf(customerRequest.cpf())).thenReturn(true);

        // when: the addCustomer method is called
        DuplicateFieldException exception = assertThrows(DuplicateFieldException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        // then: a DuplicateFieldException is thrown with the correct message
        assertEquals("Já existe um cliente cadastrado com o CPF: 12345678909.", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void whenAddCustomerWithExistingEmail_shouldThrowDuplicateFieldException() {
        // given: a customer request with an existing email
        CustomerRequest customerRequest = new CustomerRequest("Name Test", "12345678909", "test@example.com");

        when(customerRepository.existsByCpf(customerRequest.cpf())).thenReturn(false);
        when(customerRepository.existsByEmail(customerRequest.email())).thenReturn(true);

        // when: the addCustomer method is called
        DuplicateFieldException exception = assertThrows(DuplicateFieldException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        // then: a DuplicateFieldException is thrown with the correct message
        assertEquals("Já existe um cliente cadastrado com o email: test@example.com.", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }


    //given
    //when
    //then
}