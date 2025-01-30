package com.zup.e_commerce.repositories;

import com.zup.e_commerce.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<Customer> findByCpf(String cpf);
}
