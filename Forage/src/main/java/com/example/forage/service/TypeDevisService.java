package com.example.forage.service;

import com.example.forage.model.TypeDevis;
import com.example.forage.repository.TypeDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TypeDevisService {
    
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    
    /**
     * Récupérer tous les types de devis
     */
    public List<TypeDevis> getAllTypesDevis() {
        return typeDevisRepository.findAllOrderByLibelle();
    }
    
    /**
     * Récupérer tous les libellés des types de devis
     */
    public Set<String> getAllTypeDevisLibelles() {
        return typeDevisRepository.findAllLibelles();
    }
    
    /**
     * Récupérer un type de devis par son ID
     */
    public TypeDevis getTypeDevisById(Long id) {
        return typeDevisRepository.findById(id).orElse(null);
    }
    
    /**
     * Récupérer un type de devis par son libellé
     */
    public TypeDevis getTypeDevisByLibelle(String libelle) {
        return typeDevisRepository.findByLibelle(libelle).orElse(null);
    }
    
    /**
     * Ajouter un nouveau type de devis
     */
    public TypeDevis addTypeDevis(String libelle, String description) {
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé du type de devis ne peut pas être vide");
        }
        
        String libelleNormalise = libelle.trim().toUpperCase();
        
        // Vérifier si le type existe déjà
        if (typeDevisRepository.existsByLibelle(libelleNormalise)) {
            throw new IllegalArgumentException("Ce type de devis existe déjà");
        }
        
        TypeDevis typeDevis = new TypeDevis(libelleNormalise, description);
        return typeDevisRepository.save(typeDevis);
    }
    
    /**
     * Mettre à jour un type de devis
     */
    public TypeDevis updateTypeDevis(Long id, String nouveauLibelle, String description) {
        TypeDevis typeDevis = typeDevisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type de devis non trouvé"));
        
        if (nouveauLibelle != null && !nouveauLibelle.trim().isEmpty()) {
            String libelleNormalise = nouveauLibelle.trim().toUpperCase();
            
            // Vérifier si le nouveau libellé existe déjà (et n'est pas le même)
            if (!libelleNormalise.equals(typeDevis.getLibelle()) && 
                typeDevisRepository.existsByLibelle(libelleNormalise)) {
                throw new IllegalArgumentException("Ce libellé de type de devis existe déjà");
            }
            
            typeDevis.setLibelle(libelleNormalise);
        }
        
        if (description != null) {
            typeDevis.setDescription(description);
        }
        
        return typeDevisRepository.save(typeDevis);
    }
    
    /**
     * Supprimer un type de devis
     */
    public void deleteTypeDevis(Long id) {
        TypeDevis typeDevis = typeDevisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type de devis non trouvé"));
        
        // Vérifier si des devis utilisent ce type
        long count = typeDevisRepository.countDevisByType(typeDevis.getLibelle());
        if (count > 0) {
            throw new IllegalArgumentException("Impossible de supprimer ce type car il est utilisé par " + count + " devis");
        }
        
        typeDevisRepository.delete(typeDevis);
    }
    
    /**
     * Initialiser les types de devis par défaut
     */
    public void initializeDefaultTypesDevis() {
        if (typeDevisRepository.count() == 0) {
            addTypeDevis("ETUDE", "Type de devis pour les études techniques");
            addTypeDevis("FORAGE", "Type de devis pour les travaux de forage");
        }
    }
    
    /**
     * Vérifier si un type de devis existe
     */
    public boolean existsByLibelle(String libelle) {
        if (libelle == null) return false;
        return typeDevisRepository.existsByLibelle(libelle.trim().toUpperCase());
    }
}
