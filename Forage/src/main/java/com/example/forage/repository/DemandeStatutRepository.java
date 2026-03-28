package com.example.forage.repository;

import com.example.forage.model.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeStatutRepository extends JpaRepository<DemandeStatut, Long> {
    
    /**
     * Récupérer l'historique des statuts d'une demande trié par date (du plus récent au plus ancien)
     */
    @Query("SELECT ds FROM DemandeStatut ds WHERE ds.demande.id = :demandeId ORDER BY ds.date DESC")
    List<DemandeStatut> findByDemandeIdOrderByDateDesc(@Param("demandeId") Long demandeId);
    
    /**
     * Récupérer l'historique des statuts d'une demande trié par date (du plus ancien au plus récent)
     */
    @Query("SELECT ds FROM DemandeStatut ds WHERE ds.demande.id = :demandeId ORDER BY ds.date ASC")
    List<DemandeStatut> findByDemandeIdOrderByDateAsc(@Param("demandeId") Long demandeId);
    
    /**
     * Récupérer le dernier statut d'une demande
     */
    @Query("SELECT ds FROM DemandeStatut ds WHERE ds.demande.id = :demandeId ORDER BY ds.date DESC LIMIT 1")
    DemandeStatut findLastStatutByDemandeId(@Param("demandeId") Long demandeId);
    
    /**
     * Compter le nombre de changements de statut pour une demande
     */
    @Query("SELECT COUNT(ds) FROM DemandeStatut ds WHERE ds.demande.id = :demandeId")
    long countByDemandeId(@Param("demandeId") Long demandeId);
}
