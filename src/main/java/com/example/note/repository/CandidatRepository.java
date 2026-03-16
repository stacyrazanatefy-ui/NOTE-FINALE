package com.example.note.repository;

import com.example.note.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    
    Optional<Candidat> findByNom(String nom);
    
    List<Candidat> findByNomContainingIgnoreCase(String nom);
    
    boolean existsByNom(String nom);
}
