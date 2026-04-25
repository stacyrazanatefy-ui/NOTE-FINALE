package com.example.forage.repository;

import com.example.forage.model.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    
    /**
     * Récupérer les dates les plus récentes pour chaque demande
     */
    @Query("SELECT MAX(ds.date) FROM DemandeStatut ds GROUP BY ds.demande.id")
    List<LocalDateTime> findLatestDatesForAllDemandes();
    
    /**
     * Compter les demandes par statut (en utilisant les derniers statuts)
     */
    @Query("SELECT COUNT(ds) FROM DemandeStatut ds WHERE ds.statut = :statut AND ds.date IN :dates")
    int countByStatutAndDateIn(@Param("statut") com.example.forage.model.Statut statut, @Param("dates") List<LocalDateTime> dates);
    
    /**
     * Récupérer tous les derniers statuts de chaque demande
     */
    @Query("SELECT ds1 FROM DemandeStatut ds1 WHERE ds1.id IN (SELECT MAX(ds2.id) FROM DemandeStatut ds2 GROUP BY ds2.demande.id)")
    List<DemandeStatut> findAllLatestStatuts();
}
