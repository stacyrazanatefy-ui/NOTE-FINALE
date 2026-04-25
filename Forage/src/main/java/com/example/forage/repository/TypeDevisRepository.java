package com.example.forage.repository;

import com.example.forage.model.TypeDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TypeDevisRepository extends JpaRepository<TypeDevis, Long> {
    
    /**
     * Trouver un type de devis par son libellé
     */
    Optional<TypeDevis> findByLibelle(String libelle);
    
    /**
     * Vérifier si un libellé existe déjà
     */
    boolean existsByLibelle(String libelle);
    
    /**
     * Lister tous les types de devis par ordre de libellé
     */
    @Query("SELECT td FROM TypeDevis td ORDER BY td.libelle")
    List<TypeDevis> findAllOrderByLibelle();
    
    /**
     * Compter les devis qui utilisent un type donné
     */
    @Query("SELECT COUNT(d) FROM Devis d WHERE d.typeDevis = :typeDevis")
    long countDevisByType(@org.springframework.data.repository.query.Param("typeDevis") String typeDevis);
    
    /**
     * Récupérer tous les libellés des types de devis
     */
    @Query("SELECT td.libelle FROM TypeDevis td ORDER BY td.libelle")
    Set<String> findAllLibelles();
}
