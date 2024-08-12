package com.mendes.desafio_tecnico_backend_nuven.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mendes.desafio_tecnico_backend_nuven.dto.LocationDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationRequestDTO;
import com.mendes.desafio_tecnico_backend_nuven.service.LocationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;
    
    @GetMapping
    ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> dtos = locationService.findAllLocations();
        return ResponseEntity.ok().body(dtos);
    };

    @GetMapping("/{id}")
    ResponseEntity<LocationDTO> getLocationById(@PathVariable @Positive(message = "ID must be a positive Integer.") Integer id) {
        LocationDTO dto = locationService.findLocationById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationRequestDTO locationRequestDTO) {
        LocationDTO dto = locationService.saveLocation(locationRequestDTO);

        URI locationUri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(dto.getId())
                            .toUri();
        
        return ResponseEntity.created(locationUri).body(dto);
    }

    @PutMapping("/{id}")
    ResponseEntity<LocationDTO> updateLocation(
        @PathVariable @Positive(message = "ID must be a positive Integer.") Integer id, @RequestBody @Valid LocationRequestDTO locationRequestDTO
        ) {
        LocationDTO dto = locationService.updateLocation(id, locationRequestDTO);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteLocationById(@PathVariable @Positive(message = "ID must be a positive Integer.") Integer id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
