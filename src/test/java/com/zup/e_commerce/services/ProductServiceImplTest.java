package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.ProductRequest;
import com.zup.e_commerce.dtos.ProductResponse;
import com.zup.e_commerce.exceptions.DuplicateFieldException;
import com.zup.e_commerce.exceptions.ProductNotFoundException;
import com.zup.e_commerce.mappers.ProductMapper;
import com.zup.e_commerce.models.Product;
import com.zup.e_commerce.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenProductNameExistsIfAddingProductShouldThrowDuplicateFieldException() {
        // Given
        String productName = "Produto Teste";
        ProductRequest productRequest = new ProductRequest(productName, 8.00, 100);
        when(productRepository.existsByName(productName)).thenReturn(true);
        //when
        DuplicateFieldException exception = assertThrows(DuplicateFieldException.class,
                () -> productService.addProduct(productRequest));

        assertEquals("O produto com o nome \"" + productName + "\" já existe.", exception.getMessage());

        //then
        verify(productRepository).existsByName(productName);
        verify(productMapper, never()).toEntity(any(ProductRequest.class));
        verify(productRepository, never()).save(any(Product.class));
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void whenProductNameDoesNotExistIfAddingProductShouldSaveAndReturnProductResponse() {
        // Given
        String productName = "Produto Teste";
        ProductRequest productRequest = new ProductRequest(productName, 8.99, 100);
        Product product = new Product(productName, 8.99, 100);
        ProductResponse productResponse = new ProductResponse(1L, productName, 8.99, 100);

        when(productRepository.existsByName(productName)).thenReturn(false);
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // When
        ProductResponse result = productService.addProduct(productRequest);

        // Then
        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRepository).existsByName(productName);
        verify(productMapper).toEntity(productRequest);
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
    }
    
    @Test
    void whenDeletingProductByIdIfProductIdDoesNotExistShouldThrowException() {
        //given
        Long id = 4L;
        when(productRepository.existsById(id)).thenReturn(false);

        //when
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(id));

        //then
        verify(productRepository).existsById(id);
        verify(productRepository, Mockito.never()).deleteById(id);
    }

    @Test
    void whenDeletingProductByIdIfIdIsValidShouldDeleteProduct() {
        //given
        Long id = 4L;
        when(productRepository.existsById(id)).thenReturn(true);

        //when
        productService.deleteProduct(id);

        //then
        verify(productRepository).existsById(id);
        verify(productRepository).deleteById(id);
    }
}