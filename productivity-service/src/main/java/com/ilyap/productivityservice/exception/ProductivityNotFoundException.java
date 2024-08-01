package com.ilyap.productivityservice.exception;

public class ProductivityNotFoundException extends RuntimeException {

    public ProductivityNotFoundException(String id) {
        super("Productivity with id %s not found".formatted(id));
    }
}
