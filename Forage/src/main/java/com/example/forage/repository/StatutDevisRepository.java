package com.example.forage.repository;

import com.example.forage.model.StatutDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatutDevisRepository extends JpaRepository<StatutDevis, Long> {
    
    /**
     * Trouver un statut par son libellé
     */
    Optional<StatutDevis> findByLibelle(String libelle);
    
    /**
     * Vérifier si un libellé existe déjà (pour un autre ID)
     */
    boolean existsByLibelleAndIdNot(String libelle, Long id);
    
    /**
     * Compter le nombre de devis ayant ce statut
     */
    @Query("SELECT COUNT(d) FROM Devis d WHERE d.statutPersonnaliseLibelle = :libelle OR (d.statutPersonnaliseLibelle IS NULL AND d.statut = 'CREE' AND :libelle = 'Créé') OR (d.statutPersonnaliseLibelle IS NULL AND d.statut = 'ACCEPTE' AND :libelle = 'Accepté') OR (d.statutPersonnaliseLibelle IS NULL AND d.statut = 'REFUSE' AND :libelle = 'Refusé')")
    long countDevisByStatut(String libelle);
    
    /**
     * Lister tous les statuts par ordre alphabétique
     */
    List<StatutDevis> findAllByOrderByLibelleAsc();
}
