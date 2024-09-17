package com.rhb.digital.exception;

class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id : '%s'", resourceName, id));
    }
}