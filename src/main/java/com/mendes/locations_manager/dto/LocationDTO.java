package com.mendes.locations_manager.dto;

import java.time.LocalDateTime;

import com.mendes.locations_manager.model.Location;

/**
 * Data Transfer Object (DTO) for {@link Location} entity.
 * 
 * <p> This DTO is used to transfer data between layers of the application.
 */
public class LocationDTO {
        
    private Integer id;
    private String name;
    private String neighborhood;
    private String city;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    
    public LocationDTO() {}

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.neighborhood = location.getNeighborhood();
        this.city = location.getCity();
        this.state = location.getState();
        this.createdAt = location.getCreatedAt();
        this.lastUpdatedAt = location.getLastUpdatedAt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
    
}
