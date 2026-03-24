package com.example.forage.repository;

import com.example.forage.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * Recherche un client par son contact
     */
    Optional<Client> findByContact(String contact);
    
    /**
     * Recherche des clients par nom (recherche partielle)
     */
    List<Client> findByNomContainingIgnoreCase(String nom);
    
    /**
     * Vérifie si un contact existe déjà
     */
    boolean existsByContact(String contact);
    
    /**
     * Compte le nombre de clients
     */
    @Query("SELECT COUNT(c) FROM Client c")
    long countClients();
    
    /**
     * Recherche des clients avec leurs demandes
     */
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.demandes ORDER BY c.nom")
    List<Client> findAllWithDemandes();
}
