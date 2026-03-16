package com.example.note.repository;

import com.example.note.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    
    Optional<Matiere> findByNom(String nom);
    
    List<Matiere> findByNomContainingIgnoreCase(String nom);
    
    boolean existsByNom(String nom);
}
