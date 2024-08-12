package com.mendes.desafio_tecnico_backend_nuven.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Integer id) {
        super(String.format("Location with ID %s not found.", id));
    }
}
