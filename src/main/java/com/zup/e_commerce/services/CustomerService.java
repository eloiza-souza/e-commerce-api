package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;

import java.util.List;

public interface CustomerService {
   CustomerResponse addCustomer(CustomerRequest customerRequest);

   List<CustomerResponse> getAllCustomers();

   CustomerResponse getCustomerByCpf(String cpf);

   CustomerResponse updateCustomer(String cpf, CustomerRequest customerRequest);
}
