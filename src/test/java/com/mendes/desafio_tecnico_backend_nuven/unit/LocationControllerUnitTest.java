package com.mendes.desafio_tecnico_backend_nuven.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mendes.desafio_tecnico_backend_nuven.controller.LocationController;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationRequestDTO;
import com.mendes.desafio_tecnico_backend_nuven.service.LocationService;

@ExtendWith(MockitoExtension.class)
public class LocationControllerUnitTest {

    final Integer ID = 1;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private LocationDTO locationDTO1;
        
    @BeforeEach
    public void setUp() {
        locationDTO1 = new LocationDTO();
        locationDTO1.setId(1);
        locationDTO1.setName("name 1");
        locationDTO1.setNeighborhood("neighborhood 1");
        locationDTO1.setCity("city 1");
        locationDTO1.setState("state 1");
        locationDTO1.setCreatedAt(LocalDateTime.of(2024, 8, 1, 0, 0, 0));
        locationDTO1.setLastUpdatedAt(locationDTO1.getCreatedAt());
    }

    @Test
    public void whenGetAllLocations_thenReturnAllLocations() {
        LocationDTO locationDTO2 = new LocationDTO();
            locationDTO2.setId(2);
            locationDTO2.setName("name 2");
            locationDTO2.setNeighborhood("neighborhood 2");
            locationDTO2.setCity("city 2");
            locationDTO2.setState("state 2");
            locationDTO2.setCreatedAt(LocalDateTime.of(2024, 8, 1, 0, 0, 0));
            locationDTO2.setLastUpdatedAt(locationDTO2.getCreatedAt());

        List<LocationDTO> locationsDTOList = Arrays.asList(locationDTO1, locationDTO2);

        when(locationService.findAllLocations()).thenReturn(locationsDTOList);

        ResponseEntity<List<LocationDTO>> response = locationController.getAllLocations();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertIterableEquals(locationsDTOList, response.getBody());
    }

    @Test
    public void whenGetLocationById_thenReturnLocation() {
        when(locationService.findLocationById(ID)).thenReturn(locationDTO1);

        ResponseEntity<LocationDTO> result = locationController.getLocationById(ID);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(locationDTO1, result.getBody());
    }

    @Test
    public void whenPostLocation_thenReturnCreatedLocation() {
        final Integer ID_NEW_LOCATION = 3;
        final String LOCATION = String.format("http://localhost:8080/api/locations/%s", ID_NEW_LOCATION);

        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setNeighborhood("Test neighborhood");
            locationRequestDTO.setCity("Test city");
            locationRequestDTO.setState("Test state");

        LocationDTO locationResponseDTO = new LocationDTO();
            locationResponseDTO.setId(ID_NEW_LOCATION);
            locationResponseDTO.setName(locationRequestDTO.getName());
            locationResponseDTO.setNeighborhood(locationRequestDTO.getNeighborhood());
            locationResponseDTO.setCity(locationRequestDTO.getCity());
            locationResponseDTO.setState(locationRequestDTO.getState());
            locationResponseDTO.setCreatedAt(LocalDateTime.now());
            locationResponseDTO.setLastUpdatedAt(locationResponseDTO.getCreatedAt());

        when(locationService.saveLocation(locationRequestDTO)).thenReturn(locationResponseDTO);

        mockStatic(ServletUriComponentsBuilder.class);
        ServletUriComponentsBuilder ServletUriComponentsBuilderMock = mock(ServletUriComponentsBuilder.class);
        UriComponentsBuilder uriComponentsBuilderMock = mock(UriComponentsBuilder.class);
        UriComponents uriComponentsMock = mock(UriComponents.class);

        when(ServletUriComponentsBuilder.fromCurrentRequest()).thenReturn(ServletUriComponentsBuilderMock);
        when(ServletUriComponentsBuilderMock.path("/{id}")).thenReturn(uriComponentsBuilderMock);
        when(uriComponentsBuilderMock.buildAndExpand(ID_NEW_LOCATION)).thenReturn(uriComponentsMock);
        when(uriComponentsMock.toUri()).thenReturn(URI.create(LOCATION));

        ResponseEntity<LocationDTO> response = locationController.createLocation(locationRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(locationResponseDTO, response.getBody());
        assertEquals(LOCATION, response.getHeaders().get("Location").get(0));
    
}

    @Test
    public void whenUpdatLocation_thenReturnUpdatedLocation() {
        final String UPDATED_NAME = "New name";

        LocationRequestDTO requestDTO = new LocationRequestDTO();
            requestDTO.setName(UPDATED_NAME);
            requestDTO.setNeighborhood(locationDTO1.getNeighborhood());
            requestDTO.setCity(locationDTO1.getCity());
            requestDTO.setState(locationDTO1.getState());

        LocationDTO updatedLocationDTO = new LocationDTO();
            updatedLocationDTO.setId(locationDTO1.getId());
            updatedLocationDTO.setName(requestDTO.getName());
            updatedLocationDTO.setNeighborhood(requestDTO.getNeighborhood());
            updatedLocationDTO.setCity(requestDTO.getCity());
            updatedLocationDTO.setState(requestDTO.getState());
            updatedLocationDTO.setCreatedAt(locationDTO1.getCreatedAt());
            updatedLocationDTO.setLastUpdatedAt(LocalDateTime.now());
            
        when(locationService.updateLocation(ID, requestDTO)).thenReturn(updatedLocationDTO);

        ResponseEntity<LocationDTO> result = locationController.updateLocation(ID, requestDTO);
    
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedLocationDTO, result.getBody());
    }

    @Test
    public void whenDeleteLocation_thenReturnNoContent() {
        ResponseEntity<Void> result = locationController.deleteLocationById(ID);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

}
