package com.mendes.desafio_tecnico_backend_nuven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mendes.desafio_tecnico_backend_nuven.model.Location;

/**
 * Repository interface responsible for managing {@link Location} entities.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>{
    
    /**
     * Retrieves all {@link Location} entities ordered by their creation date in ascending order.
     *
     * @return A list of {@link Location} entities sorted by the {@code createdAt} field in ascending order.
     */
    List<Location> findAllByOrderByCreatedAtAsc();
}
