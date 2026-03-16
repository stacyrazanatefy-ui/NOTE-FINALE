package com.example.note.repository;

import com.example.note.entity.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
    
    Optional<Resolution> findByNom(String nom);
    
    List<Resolution> findByNomContainingIgnoreCase(String nom);
    
    boolean existsByNom(String nom);
}
