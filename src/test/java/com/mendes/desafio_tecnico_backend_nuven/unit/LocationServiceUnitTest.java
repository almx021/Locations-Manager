package com.mendes.desafio_tecnico_backend_nuven.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mendes.desafio_tecnico_backend_nuven.dto.LocationDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationRequestDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.mapper.LocationMapper;
import com.mendes.desafio_tecnico_backend_nuven.exception.LocationNotFoundException;
import com.mendes.desafio_tecnico_backend_nuven.model.Location;
import com.mendes.desafio_tecnico_backend_nuven.repository.LocationRepository;
import com.mendes.desafio_tecnico_backend_nuven.service.LocationService;

@ExtendWith(MockitoExtension.class)
public class LocationServiceUnitTest {
    
    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationService locationService;

    private Location location1;
    
    @BeforeEach
    public void setUp() {
        location1 = new Location();
        location1.setId(1);
        location1.setName("name 1");
        location1.setNeighborhood("neighborhood 1");
        location1.setCity("city 1");
        location1.setState("state 1");
        location1.setCreatedAt(LocalDateTime.of(2024, 8, 1, 0, 0, 0));
        location1.setLastUpdatedAt(location1.getCreatedAt());
    }

    @Test
    public void whenFindingAllLocations_thenLocationDTOListShouldBeReturned() {
        Location location2 = new Location();
            location2.setId(2);
            location2.setName("name 2");
            location2.setNeighborhood("neighborhood 2");
            location2.setCity("city 2");
            location2.setState("state 2");
            location2.setCreatedAt(LocalDateTime.of(2024, 8, 1, 0, 0, 1));
            location2.setLastUpdatedAt(location2.getCreatedAt());

        LocationDTO locationDTO1 = new LocationDTO(location1);
        LocationDTO locationDTO2 = new LocationDTO(location2);

        when(locationRepository.findAllByOrderByCreatedAtAsc()).thenReturn(Arrays.asList(location1, location2));
        when(locationMapper.toDTO(location1)).thenReturn(locationDTO1);
        when(locationMapper.toDTO(location2)).thenReturn(locationDTO2);

        List<LocationDTO> result = locationService.findAllLocations();

        verify(locationRepository, times(1)).findAllByOrderByCreatedAtAsc();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(locationDTO1, result.get(0));
        assertEquals(locationDTO2, result.get(1));
    }

    @Test
    void whenNoLocationWasFound_thenEmptyListShouldBeReturned() {
        when(locationRepository.findAllByOrderByCreatedAtAsc()).thenReturn(Arrays.asList());

        List<LocationDTO> result = locationService.findAllLocations();

        verify(locationRepository, times(1)).findAllByOrderByCreatedAtAsc();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void whenLocationIsFoundById_thenLocationDTOShouldBeReturned() {
        final Integer ID = 1;
        when(locationRepository.findById(ID)).thenReturn(Optional.of(location1));

        LocationDTO locationDTO1 = new LocationDTO(location1);
        when(locationMapper.toDTO(location1)).thenReturn(locationDTO1);
        
        LocationDTO result = locationService.findLocationById(ID);
        
        verify(locationRepository, times(1)).findById(ID);
        verify(locationMapper, times(1)).toDTO(location1);
        
        assertNotNull(result);
        assertEquals(locationDTO1, result);
    }

    @Test
    void whenLocationIsNotFound_thenNoLocationFoundExceptionShouldBeRaised() {
        final Integer ID = 0;

        when(locationRepository.findById(ID)).thenReturn(Optional.empty());
    
        LocationNotFoundException exception = assertThrows(LocationNotFoundException.class, 
                                                            () -> locationService.findLocationById(ID));

        verify(locationRepository, times(1)).findById(ID);

        assertNotNull(exception);
        assertEquals(exception.getMessage(), String.format("Location with ID %s not found.", ID));
    }

    @Test
    void whenLocationIsSaved_thenLocationDTOShouldBeReturned() {
        LocationRequestDTO locationRequestDTO = new LocationRequestDTO(location1);
        when(locationMapper.toEntity(locationRequestDTO)).thenReturn(location1);

        LocationDTO locationDTO1 = new LocationDTO(location1);
        when(locationMapper.toDTO(location1)).thenReturn(locationDTO1);

        when(locationRepository.save(location1)).thenReturn(location1);

        LocationDTO result = locationService.saveLocation(locationRequestDTO);

        verify(locationMapper, times(1)).toEntity(locationRequestDTO);
        verify(locationRepository, times(1)).save(location1);
        verify(locationMapper, times(1)).toDTO(location1);

        assertNotNull(result);
        assertEquals(locationDTO1, result);
    }

    @Test
    void whenLocationIsUpdated_thenUpdatedLocationDTOShouldBeReturned() {
        final Integer ID = 1;
        final String UPDATED_NAME = "New name";

        LocationRequestDTO requestDTO = new LocationRequestDTO();
            requestDTO.setName(UPDATED_NAME);
            requestDTO.setNeighborhood(location1.getNeighborhood());
            requestDTO.setCity(location1.getCity());
            requestDTO.setState(location1.getState());

        Location updatedLocation = new Location();
            updatedLocation.setId(location1.getId());
            updatedLocation.setName(requestDTO.getName());
            updatedLocation.setNeighborhood(requestDTO.getNeighborhood());
            updatedLocation.setCity(requestDTO.getCity());
            updatedLocation.setState(requestDTO.getState());
            updatedLocation.setCreatedAt(location1.getCreatedAt());
            updatedLocation.setLastUpdatedAt(location1.getCreatedAt().plusMinutes(1));

        LocationDTO updatedLocationDTO = new LocationDTO(updatedLocation);

        when(locationRepository.findById(ID)).thenReturn(Optional.of(location1));
        when(locationRepository.save(location1)).thenReturn(updatedLocation);
        when(locationMapper.toDTO(updatedLocation)).thenReturn(updatedLocationDTO);
       
        LocationDTO result = locationService.updateLocation(ID, requestDTO);

        verify(locationMapper, times(1)).toDTO(updatedLocation);
        
        assertNotNull(result);
        assertEquals(updatedLocationDTO, result);
    }

    @Test
    void whenDeletingLocationById_thenRepositoryDeleteByIdShouldBeCalled() {
        final Integer ID = 1;

        locationService.deleteLocationById(ID);

        verify(locationRepository, times(1)).deleteById(ID);
    }
}
