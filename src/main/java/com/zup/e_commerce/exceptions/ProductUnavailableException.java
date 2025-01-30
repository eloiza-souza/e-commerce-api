package com.zup.e_commerce.exceptions;

public class ProductUnavailableException extends RuntimeException{
    public ProductUnavailableException(String message) {
        super(message);
    }
}
