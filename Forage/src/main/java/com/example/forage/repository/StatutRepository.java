package com.example.forage.repository;

import com.example.forage.model.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Long> {
    
    /**
     * Trouver un statut par son libellé
     */
    Optional<Statut> findByLibelle(String libelle);
    
    /**
     * Vérifier si un libellé existe déjà
     */
    boolean existsByLibelle(String libelle);
    
    /**
     * Lister tous les statuts par ordre de libellé
     */
    @Query("SELECT s FROM Statut s ORDER BY s.libelle")
    List<Statut> findAllOrderByLibelle();
    
    /**
     * Vérifier si un statut est utilisé dans des demandes
     */
    @Query("SELECT COUNT(ds) > 0 FROM DemandeStatut ds WHERE ds.statut.id = :statutId")
    boolean isStatutUsedInDemandes(@Param("statutId") Long statutId);
}
