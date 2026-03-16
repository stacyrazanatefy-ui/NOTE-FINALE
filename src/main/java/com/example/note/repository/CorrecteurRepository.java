package com.example.note.repository;

import com.example.note.entity.Correcteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorrecteurRepository extends JpaRepository<Correcteur, Long> {
    
    Optional<Correcteur> findByNom(String nom);
    
    List<Correcteur> findByNomContainingIgnoreCase(String nom);
    
    boolean existsByNom(String nom);
}
