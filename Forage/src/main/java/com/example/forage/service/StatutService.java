package com.example.forage.service;

import com.example.forage.model.Statut;
import com.example.forage.repository.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutService {
    
    @Autowired
    private StatutRepository statutRepository;
    
    /**
     * Sauvegarder un statut
     */
    public Statut saveStatut(Statut statut) {
        return statutRepository.save(statut);
    }
    
    /**
     * Récupérer tous les statuts
     */
    public List<Statut> getAllStatuts() {
        return statutRepository.findAllOrderByLibelle();
    }
    
    /**
     * Récupérer un statut par son ID
     */
    public Optional<Statut> getStatutById(Long id) {
        return statutRepository.findById(id);
    }
    
    /**
     * Récupérer un statut par son libellé
     */
    public Optional<Statut> getStatutByLibelle(String libelle) {
        return statutRepository.findByLibelle(libelle);
    }
    
    /**
     * Supprimer un statut par son ID
     */
    public void deleteStatut(Long id) {
        statutRepository.deleteById(id);
    }
    
    /**
     * Vérifier si un libellé existe
     */
    public boolean existsByLibelle(String libelle) {
        return statutRepository.existsByLibelle(libelle);
    }
    
    /**
     * Initialiser les statuts par défaut
     */
    public void initializeDefaultStatuts() {
        if (statutRepository.count() == 0) {
            createDefaultStatut("DEMANDE_CREEE");
            createDefaultStatut("DEMANDE_ANNULEE");
            createDefaultStatut("DEMANDE_TERMINEE");
        }
    }
    
    /**
     * Créer un statut par défaut s'il n'existe pas
     */
    private void createDefaultStatut(String libelle) {
        if (!existsByLibelle(libelle)) {
            Statut statut = new Statut(libelle);
            statutRepository.save(statut);
        }
    }
}
