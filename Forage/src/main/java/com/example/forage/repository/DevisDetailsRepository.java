package com.example.forage.repository;

import com.example.forage.model.DevisDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevisDetailsRepository extends JpaRepository<DevisDetails, Long> {
    
    /**
     * Lister tous les détails d'un devis
     */
    List<DevisDetails> findByDevisIdOrderByLibelle(Long devisId);
    
    /**
     * Compter le nombre de détails pour un devis
     */
    long countByDevisId(Long devisId);
    
    /**
     * Supprimer tous les détails d'un devis
     */
    void deleteByDevisId(Long devisId);
    
    /**
     * Trouver un détail par son ID avec le devis associé
     */
    @Query("SELECT dd FROM DevisDetails dd LEFT JOIN FETCH dd.devis d LEFT JOIN FETCH dd.devis.demande WHERE dd.id = :id")
    DevisDetails findByIdWithDevis(@Param("id") Long id);
    
    /**
     * Calculer le montant total d'un devis
     */
    @Query("SELECT COALESCE(SUM(dd.montant), 0) FROM DevisDetails dd WHERE dd.devis.id = :devisId")
    Double calculateTotalByDevisId(@Param("devisId") Long devisId);
}
