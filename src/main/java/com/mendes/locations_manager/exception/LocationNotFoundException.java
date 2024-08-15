package com.mendes.locations_manager.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Integer id) {
        super(String.format("Location with ID %s not found.", id));
    }
}
