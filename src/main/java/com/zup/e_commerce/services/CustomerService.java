package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.CustomerRequest;
import com.zup.e_commerce.dtos.CustomerResponse;

public interface CustomerService {
   CustomerResponse addCustomer(CustomerRequest customerRequest);
   CustomerResponse getCustomerByCpf(String cpf);
   CustomerResponse updateCustomer(String cpf, CustomerRequest customerRequest);
}
