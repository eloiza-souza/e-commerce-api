package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.ProductRequest;
import com.zup.e_commerce.dtos.ProductResponse;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.exceptions.ProductNotFoundException;
import com.zup.e_commerce.models.Product;
import com.zup.e_commerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        String name = productRequest.name();
        if (productRepository.existsByName(name)){
            throw new DuplicateFieldException("O produto com o nome \"" + name + "\" já existe.");
        }
        Product product = mapToEntity(productRequest);
        productRepository.save(product);
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)){
            throw new ProductNotFoundException("Produto com id " + id + " não encontrado.");
        }
        productRepository.deleteById(id);
    }

    private Product mapToEntity(ProductRequest productRequest) {
        return new Product(productRequest.name(),
                productRequest.price(),
                productRequest.quantity());
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity());
    }
}
