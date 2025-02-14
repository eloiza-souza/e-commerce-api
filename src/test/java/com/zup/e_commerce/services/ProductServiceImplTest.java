package com.zup.e_commerce.services;

import com.zup.e_commerce.exceptions.ProductNotFoundException;
import com.zup.e_commerce.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void whenDeletingProductByIdIfProductIdDoesNotExistShouldThrowException() {
        //given
        Long id = 4L;
        when(productRepository.existsById(id)).thenReturn(false);

        //when
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(id));

        //then
        verify(productRepository, Mockito.times(1))
                .existsById(id);
        verify(productRepository, Mockito.never())
                .deleteById(id);
    }

    @Test
    void whenDeletingProductByIdIfIdIsValidShouldDeleteProduct() {
        //given
        Long id = 4L;
        when(productRepository.existsById(id)).thenReturn(true);

        //when
        productService.deleteProduct(id);

        //then
        verify(productRepository, Mockito.times(1))
                .existsById(id);
        verify(productRepository, Mockito.times(1))
                .deleteById(id);
    }
}