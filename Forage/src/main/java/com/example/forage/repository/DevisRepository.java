package com.example.forage.repository;

import com.example.forage.model.Devis;
import com.example.forage.model.Devis.StatutDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {
    
    /**
     * Trouver un devis par son ID avec la demande associée
     */
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.demande LEFT JOIN FETCH d.demande.client WHERE d.id = :id")
    Optional<Devis> findByIdWithDemande(@Param("id") Long id);
    
    /**
     * Trouver un devis par son ID avec tous les détails
     */
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.demande LEFT JOIN FETCH d.demande.client LEFT JOIN FETCH d.details WHERE d.id = :id")
    Optional<Devis> findByIdWithDetails(@Param("id") Long id);
    
    /**
     * Lister tous les devis avec demande et client
     */
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.demande LEFT JOIN FETCH d.demande.client ORDER BY d.dateCreation DESC")
    List<Devis> findAllWithDemandeAndClient();
    
    /**
     * Lister les devis par statut
     */
    List<Devis> findByStatutOrderByDateCreationDesc(StatutDevis statut);
    
    /**
     * Lister les devis par type
     */
    List<Devis> findByTypeDevisOrderByDateCreationDesc(String typeDevis);
    
    /**
     * Lister les devis par demande
     */
    List<Devis> findByDemandeIdOrderByDateCreationDesc(Long demandeId);
    
    /**
     * Lister les devis par client
     */
    @Query("SELECT d FROM Devis d JOIN d.demande dem JOIN dem.client cli WHERE cli.id = :clientId ORDER BY d.dateCreation DESC")
    List<Devis> findByClientIdOrderByDateCreationDesc(@Param("clientId") Long clientId);
    
    /**
     * Compter les devis par statut
     */
    long countByStatut(StatutDevis statut);
    
    /**
     * Compter les devis par type
     */
    long countByTypeDevis(String typeDevis);
    
    /**
     * Rechercher des devis par période
     */
    @Query("SELECT d FROM Devis d WHERE d.dateCreation BETWEEN :startDate AND :endDate ORDER BY d.dateCreation DESC")
    List<Devis> findByDateCreationBetweenOrderByDateCreationDesc(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Vérifier si un devis existe pour une demande et un type
     */
    boolean existsByDemandeIdAndTypeDevis(Long demandeId, String typeDevis);
    
    /**
     * Trouver le dernier devis d'une demande
     */
    Optional<Devis> findFirstByDemandeIdOrderByDateCreationDesc(Long demandeId);
}
