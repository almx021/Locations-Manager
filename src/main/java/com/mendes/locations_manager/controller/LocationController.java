package com.mendes.locations_manager.controller;

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

import com.mendes.locations_manager.dto.LocationDTO;
import com.mendes.locations_manager.dto.LocationRequestDTO;
import com.mendes.locations_manager.exception.ResponseError;
import com.mendes.locations_manager.service.LocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * Controller layers resposible for providing endpoints for creating, retrieving, updating, and deleting locations.
 */
@Tag(name = "Locations", description = "Endpoints for managing locations")
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;
    
    @Operation(
        summary = "Fetches all locations",
        description = "Fetches all locations ordered by creation date",
        responses = {
            @ApiResponse(
                description = "Locations found successfully", 
                responseCode = "200", 
                content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LocationDTO.class)))}),
            @ApiResponse(
                description = "Internal server error", 
                responseCode = "500", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> dtos = locationService.findAllLocations();
        return ResponseEntity.ok().body(dtos);
    };

    @Operation(
        summary = "Fetches a location",
        description = "Fetches a location by its ID",
        responses = {
            @ApiResponse(
                description = "Location found successfully",
                responseCode = "200",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))
            ),
            @ApiResponse(
                description = "Validation error: Invalid ID was sent", 
                responseCode = "400", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(
                description = "No location was found with such ID", 
                responseCode = "404", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(
                description = "Internal server error", 
                responseCode = "500", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable @Positive(message = "ID must be a positive Integer.") Integer id) {
        LocationDTO dto = locationService.findLocationById(id);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
        summary = "Creates a location",
        description = "Creates a new location and then returns it. All fields (name, neighborhood, city, state) are mandatory",
        responses = {
            @ApiResponse(
                description = "Location created successfully", 
                responseCode = "201",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))
            ),
            @ApiResponse(
                description = "Validation error: Invalid data was sent", 
                responseCode = "400",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(
                description = "Internal server error", 
                responseCode = "500", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationRequestDTO locationRequestDTO) {
        LocationDTO dto = locationService.saveLocation(locationRequestDTO);

        URI locationUri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(dto.getId())
                            .toUri();
        
        return ResponseEntity.created(locationUri).body(dto);
    }

    @Operation(
        summary = "Updates a location",
        description = "Fetches if a location exists and then updates it",
        responses = {
            @ApiResponse(
                description = "Location updated Successfully",
                responseCode = "200",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))
            ),
            @ApiResponse(
                description = "Validation error: Invalid ID or data was sent",
                responseCode = "400",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(
                description = "No location was found with such ID", 
                responseCode = "404", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(
                description = "Internal server error", 
                responseCode = "500", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable @Positive(message = "ID must be a positive Integer.") Integer id, 
                                                        @RequestBody @Valid LocationRequestDTO locationRequestDTO) {
        LocationDTO dto = locationService.updateLocation(id, locationRequestDTO);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
        summary = "Deletes a location",
        responses = {
            @ApiResponse(
                description = "Location deleted successfully",
                responseCode = "204",
                content = @Content
            ),
            @ApiResponse(
                description = "Internal server error", 
                responseCode = "500", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable @Positive(message = "ID must be a positive Integer.") Integer id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
