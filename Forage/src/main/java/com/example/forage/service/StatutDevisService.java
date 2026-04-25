package com.example.forage.service;

import com.example.forage.model.StatutDevis;
import com.example.forage.repository.StatutDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutDevisService {
    
    @Autowired
    private StatutDevisRepository statutDevisRepository;
    
    /**
     * Lister tous les statuts de devis
     */
    public List<StatutDevis> getAllStatutsDevis() {
        return statutDevisRepository.findAllByOrderByLibelleAsc();
    }
    
    /**
     * Trouver un statut par son ID
     */
    public Optional<StatutDevis> getStatutDevisById(Long id) {
        return statutDevisRepository.findById(id);
    }
    
    /**
     * Trouver un statut par son libellé
     */
    public Optional<StatutDevis> getStatutDevisByLibelle(String libelle) {
        return statutDevisRepository.findByLibelle(libelle);
    }
    
    /**
     * Créer un nouveau statut de devis
     */
    public StatutDevis createStatutDevis(StatutDevis statutDevis) {
        // Vérifier si le libellé existe déjà
        if (statutDevisRepository.findByLibelle(statutDevis.getLibelle()).isPresent()) {
            throw new IllegalArgumentException("Un statut avec le libellé '" + statutDevis.getLibelle() + "' existe déjà");
        }
        return statutDevisRepository.save(statutDevis);
    }
    
    /**
     * Mettre à jour un statut de devis
     */
    public StatutDevis updateStatutDevis(StatutDevis statutDevis) {
        // Vérifier si le statut existe
        if (!statutDevisRepository.existsById(statutDevis.getId())) {
            throw new IllegalArgumentException("Statut non trouvé avec l'ID: " + statutDevis.getId());
        }
        
        // Vérifier si le libellé existe déjà pour un autre statut
        if (statutDevisRepository.existsByLibelleAndIdNot(statutDevis.getLibelle(), statutDevis.getId())) {
            throw new IllegalArgumentException("Un statut avec le libellé '" + statutDevis.getLibelle() + "' existe déjà");
        }
        
        return statutDevisRepository.save(statutDevis);
    }
    
    /**
     * Supprimer un statut de devis
     */
    public void deleteStatutDevis(Long id) {
        // Vérifier si le statut existe
        if (!statutDevisRepository.existsById(id)) {
            throw new IllegalArgumentException("Statut non trouvé avec l'ID: " + id);
        }
        
        Optional<StatutDevis> statutDevis = statutDevisRepository.findById(id);
        if (statutDevis.isPresent()) {
            // Vérifier si des devis utilisent ce statut
            long countDevis = statutDevisRepository.countDevisByStatut(statutDevis.get().getLibelle());
            if (countDevis > 0) {
                throw new IllegalArgumentException("Impossible de supprimer ce statut car il est utilisé par " + countDevis + " devis");
            }
        }
        
        statutDevisRepository.deleteById(id);
    }
    
    /**
     * Initialiser les statuts par défaut
     */
    public void initializeDefaultStatuts() {
        if (statutDevisRepository.count() == 0) {
            createStatutDevis(new StatutDevis("CRÉÉ", "Le devis vient d'être créé et est en attente de validation."));
            createStatutDevis(new StatutDevis("ACCEPTÉ", "Le devis a été accepté par le client et peut être mis en exécution."));
            createStatutDevis(new StatutDevis("REFUSÉ", "Le devis a été refusé par le client ou n'est plus valide."));
        }
    }
}
