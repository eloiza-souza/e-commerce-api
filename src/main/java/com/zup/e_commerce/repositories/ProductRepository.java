package com.zup.e_commerce.repositories;

import com.zup.e_commerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsById(Long id);
    void deleteById(Long id);
}
