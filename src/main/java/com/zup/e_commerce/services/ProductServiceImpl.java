package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.ProductRequest;
import com.zup.e_commerce.dtos.ProductResponse;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.exceptions.ProductNotFoundException;
import com.zup.e_commerce.mappers.ProductMapper;
import com.zup.e_commerce.models.Product;
import com.zup.e_commerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        if(productRequest == null){
            throw new IllegalArgumentException("ProductRequest não pode ser nulo");
        }
        String name = productRequest.name();
        if (productRepository.existsByName(name)) {
            throw new DuplicateFieldException("O produto com o nome \"" + name + "\" já existe.");
        }
        Product product = productMapper.toEntity(productRequest);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Produto com id " + id + " não encontrado.");
        }
        productRepository.deleteById(id);
    }
}