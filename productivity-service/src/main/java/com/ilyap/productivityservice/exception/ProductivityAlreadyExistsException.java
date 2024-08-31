package com.ilyap.productivityservice.exception;

import java.util.UUID;

public class ProductivityAlreadyExistsException extends RuntimeException {

    public ProductivityAlreadyExistsException(UUID id) {
        super("Productivity with id %s already exists".formatted(id));
    }

    public ProductivityAlreadyExistsException(String message){
        super(message);
    }
}
