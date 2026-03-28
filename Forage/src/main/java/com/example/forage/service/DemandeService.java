package com.example.forage.service;

import com.example.forage.model.Demande;
import com.example.forage.model.DemandeStatut;
import com.example.forage.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeService {
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private DemandeStatutService demandeStatutService;
    
    /**
     * Sauvegarde une demande avec ajout automatique du statut initial
     */
    public Demande saveDemande(Demande demande) {
        Demande savedDemande = demandeRepository.save(demande);
        // Ajouter automatiquement le statut "DEMANDE_CREEE"
        demandeStatutService.addStatutInitial(savedDemande);
        return savedDemande;
    }
    
    /**
     * Récupère toutes les demandes
     */
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }
    
    /**
     * Récupère toutes les demandes avec informations du client
     */
    public List<Demande> getAllDemandesWithClient() {
        return demandeRepository.findAllWithClient();
    }
    
    /**
     * Récupère une demande par son ID
     */
    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepository.findById(id);
    }
    
    /**
     * Récupère les demandes par client
     */
    public List<Demande> getDemandesByClientId(Long clientId) {
        return demandeRepository.findByClientId(clientId);
    }
    
    /**
     * Recherche des demandes par lieu
     */
    public List<Demande> searchDemandesByLieu(String lieu) {
        return demandeRepository.findByLieuContainingIgnoreCase(lieu);
    }
    
    /**
     * Recherche des demandes par district
     */
    public List<Demande> searchDemandesByDistrict(String district) {
        return demandeRepository.findByDistrictContainingIgnoreCase(district);
    }
    
    /**
     * Recherche des demandes dans une période de dates
     */
    public List<Demande> searchDemandesByDateRange(LocalDate startDate, LocalDate endDate) {
        return demandeRepository.findByDateBetween(startDate, endDate);
    }
    
    /**
     * Récupère les demandes à partir d'une date
     */
    public List<Demande> getDemandesAfterDate(LocalDate date) {
        return demandeRepository.findByDateAfter(date);
    }
    
    /**
     * Met à jour une demande
     */
    public Demande updateDemande(Long id, Demande demandeDetails) {
        Optional<Demande> optionalDemande = demandeRepository.findById(id);
        if (optionalDemande.isPresent()) {
            Demande demande = optionalDemande.get();
            demande.setDate(demandeDetails.getDate());
            demande.setLieu(demandeDetails.getLieu());
            demande.setDistrict(demandeDetails.getDistrict());
            demande.setClient(demandeDetails.getClient());
            return demandeRepository.save(demande);
        }
        return null;
    }
    
    /**
     * Supprime une demande par son ID et son historique de statuts
     */
    @Transactional
    public boolean deleteDemande(Long id) {
        if (demandeRepository.existsById(id)) {
            // Supprimer d'abord l'historique des statuts
            demandeStatutService.deleteStatutsByDemandeId(id);
            // Puis supprimer la demande
            demandeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Compte le nombre de demandes par client
     */
    public long countDemandesByClientId(Long clientId) {
        return demandeRepository.countByClientId(clientId);
    }
    
    /**
     * Compte le nombre total de demandes
     */
    public long countDemandes() {
        return demandeRepository.countDemandes();
    }
    
    /**
     * Récupère l'historique des statuts d'une demande
     */
    public List<DemandeStatut> getHistoriqueStatuts(Long demandeId) {
        return demandeStatutService.getHistoriqueStatutsByDemandeId(demandeId);
    }
    
    /**
     * Récupère le dernier statut d'une demande
     */
    public Optional<DemandeStatut> getLastStatut(Long demandeId) {
        return demandeStatutService.getLastStatutByDemandeId(demandeId);
    }
    
    /**
     * Change le statut d'une demande
     */
    public DemandeStatut changerStatutDemande(Long demandeId, String nouveauStatut) {
        return demandeStatutService.changerStatutDemande(demandeId, nouveauStatut);
    }
}
