package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;
import com.zup.e_commerce.exceptions.CustomerNotFoundException;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.mappers.CustomerMapper;
import com.zup.e_commerce.models.Customer;
import com.zup.e_commerce.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    void whenAddCustomerWithNullRequest_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.addCustomer(null);
        });
        assertEquals("CustomerRequest não pode ser nulo.", exception.getMessage());
    }

    @Test
    public void whenAddCustomerWithValidRequest_shouldSaveAndReturnCustomerResponse() {
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

    @Test
    void whenGetAllCustomersAndCustomersExists_shouldReturnListOfCustomerResponses() {
       //given
        Customer customer1 = new Customer("Name1", "12345678901", "email1@example.com");
        Customer customer2 = new Customer("Name2", "98765432109", "email2@example.com");
        CustomerResponse response1 = new CustomerResponse(1L, "Name1", "12345678901", "email1@example.com");
        CustomerResponse response2 = new CustomerResponse(2L, "Name2", "98765432109", "email2@example.com");

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));
        when(customerMapper.toResponse(customer1)).thenReturn(response1);
        when(customerMapper.toResponse(customer2)).thenReturn(response2);

        //when
        List<CustomerResponse> result = customerService.getAllCustomers();

        //then
        assertNotNull(result, "A lista de respostas não deve ser nula.");
        assertEquals(2, result.size(), "A lista deve conter 2 elementos.");
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
    }

    @Test
    void whenGetAllCustomersAndNoCustomersExists_shouldReturnListOfCustomerResponses() {
        //given
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<CustomerResponse> result = customerService.getAllCustomers();

        //then
        assertNotNull(result, "A lista de respostas não deve ser nula.");
        assertTrue(result.isEmpty(),"A lista deve estar vazia.");

        verify(customerRepository).findAll();
        verify(customerMapper, never()).toResponse(any(Customer.class));

    }

    @Test
    public void whenGetCustomerByCpfAndCpfIsNull_shouldThrowIllegalArgumentException(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerByCpf(null);
        });
        assertEquals("CPF não pode ser nulo.", exception.getMessage());
    }

    @Test
    public void whenGetCustomerByCpfAndCpfExists_shouldReturnCustomerResponse(){
        //given
        String cpf = "12345678909";
        Customer customer = new Customer("Name Test", cpf, "email@test.com");
        CustomerResponse response = new CustomerResponse(1L, "Name Test", cpf, "email@test.com");

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponse(customer)).thenReturn(response);

        //when
        CustomerResponse result = customerService.getCustomerByCpf(cpf);

        //then
        assertNotNull(result, "A resposta não deve ser nula.");
        assertEquals(response, result);
    }

    @Test
    public void whenGetCustomerByCpfAndCpfDoesNotExists_shouldReturnCustomerResponse(){
        //given
        String cpf = "12345678909";
        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        //when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByCpf(cpf));

        // then
        assertEquals("Cliente com CPF: " + cpf + " não encontrado.", exception.getMessage());
        verify(customerMapper, never()).toResponse(any());
    }

}