package com.mendes.desafio_tecnico_backend_nuven.dto;

import com.mendes.desafio_tecnico_backend_nuven.model.Location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocationRequestDTO {
    
    @Size(max = 50, message = "The field `name` can have a maximum of {max} characters.")
    @NotBlank(message = "The field `name` cannot be empty.")
    private String name;

    @Size(max = 50, message = "The field `neighborhood` can have a maximum of {max} characters.")
    @NotBlank(message = "The field `neighborhood` cannot be empty.")
    private String neighborhood;
    
    @Size(max = 50, message = "The field `city` can have a maximum of {max} characters.")
    @NotBlank(message = "The field `city` cannot be empty.")
    private String city;

    @Size(max = 50, message = "The field `state` can have a maximum of {max} characters.")
    @NotBlank(message = "The field `state` cannot be empty.")
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocationRequestDTO() {}

    public LocationRequestDTO(Location location) {
        this.name = location.getName();
        this.neighborhood = location.getNeighborhood();
        this.city = location.getCity();
        this.state = location.getState();
    }

    
}
