package com.mendes.desafio_tecnico_backend_nuven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mendes.desafio_tecnico_backend_nuven.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>{
    
    List<Location> findAllByOrderByCreatedAtAsc();
}
