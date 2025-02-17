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
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

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
    void whenAddProductWithNullRequest_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(null);
        });
        assertEquals("ProductRequest não pode ser nulo", exception.getMessage());
    }

    @Test
    void whenAddProductWithExistingName_shouldThrowDuplicateFieldException() {
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

    }

    @Test
    void whenAddProductWithNotExistingProductName_shouldSaveAndReturnProductResponse() {
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
    void whenGetAllProductsAndProductsExist_shouldReturnListOfProductResponses() {
        // Given
        Product product1 = new Product("Produto 1", 5.98, 100);
        product1.setId(1L);
        Product product2 = new Product("Produto 2", 6.58, 200);
        product2.setId(2L);
        ProductResponse response1 = new ProductResponse(1L, "Produto 1", 5.98, 100);
        ProductResponse response2 = new ProductResponse(2L, "Produto 2", 6.58, 200);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productMapper.toResponse(product1)).thenReturn(response1);
        when(productMapper.toResponse(product2)).thenReturn(response2);

        // When
        List<ProductResponse> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(productRepository).findAll();
        verify(productMapper).toResponse(product1);
        verify(productMapper).toResponse(product2);
    }

    @Test
    void whenGetAllProductsAndNoProductsExist_shouldReturnEmptyList() {
        // Given
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ProductResponse> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository).findAll();
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void whenDeleteProductByIdIfProductIdDoesNotExist_shouldThrowException() {
        //given
        Long id = 4L;
        when(productRepository.existsById(id)).thenReturn(false);

        //when
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(id));

        //then
        verify(productRepository).existsById(id);
        verify(productRepository, never()).deleteById(id);
    }

    @Test
    void whenDeleteProductByIdIfIdIsValid_shouldDeleteProduct() {
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