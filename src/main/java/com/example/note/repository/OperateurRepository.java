package com.example.note.repository;

import com.example.note.entity.Operateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Long> {
    
    Optional<Operateur> findByOperateur(String operateur);
    
    List<Operateur> findByOperateurContainingIgnoreCase(String operateur);
    
    boolean existsByOperateur(String operateur);
}
