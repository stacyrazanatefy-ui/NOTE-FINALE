package com.example.forage.repository;

import com.example.forage.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    
    /**
     * Recherche des demandes par client
     */
    List<Demande> findByClientId(Long clientId);
    
    /**
     * Recherche des demandes par lieu (recherche partielle)
     */
    List<Demande> findByLieuContainingIgnoreCase(String lieu);
    
    /**
     * Recherche des demandes par district (recherche partielle)
     */
    List<Demande> findByDistrictContainingIgnoreCase(String district);
    
    /**
     * Recherche des demandes dans une période de dates
     */
    List<Demande> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Recherche des demandes à partir d'une date
     */
    List<Demande> findByDateAfter(LocalDate date);
    
    /**
     * Compte le nombre de demandes par client
     */
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.client.id = :clientId")
    long countByClientId(@Param("clientId") Long clientId);
    
    /**
     * Recherche des demandes avec informations du client
     */
    @Query("SELECT d FROM Demande d LEFT JOIN FETCH d.client ORDER BY d.date DESC")
    List<Demande> findAllWithClient();
    
    /**
     * Compte le nombre total de demandes
     */
    @Query("SELECT COUNT(d) FROM Demande d")
    long countDemandes();
    
    /**
     * Recherche des demandes par terme générique
     */
    @Query("SELECT d FROM Demande d LEFT JOIN FETCH d.client c WHERE " +
           "LOWER(d.lieu) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.district) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contact) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Demande> searchDemandes(@Param("searchTerm") String searchTerm);
    
    /**
     * Récupère les demandes par statut
     */
    @Query("SELECT DISTINCT d FROM Demande d " +
           "JOIN d.demandeStatuts ds " +
           "JOIN ds.statut s " +
           "WHERE s.libelle = :statutLibelle " +
           "AND ds.date = (SELECT MAX(ds2.date) FROM DemandeStatut ds2 WHERE ds2.demande.id = d.id)")
    List<Demande> findByStatutLibelle(@Param("statutLibelle") String statutLibelle);
    
    /**
     * Récupère les demandes avant une date
     */
    @Query("SELECT d FROM Demande d WHERE d.date <= :endDate ORDER BY d.date DESC")
    List<Demande> findByDateBefore(@Param("endDate") LocalDate endDate);
    
    /**
     * Récupère tous les lieux uniques
     */
    @Query("SELECT DISTINCT d.lieu FROM Demande d ORDER BY d.lieu")
    List<String> findAllDistinctLieux();
    
    /**
     * Récupère tous les districts uniques
     */
    @Query("SELECT DISTINCT d.district FROM Demande d ORDER BY d.district")
    List<String> findAllDistinctDistricts();
}
