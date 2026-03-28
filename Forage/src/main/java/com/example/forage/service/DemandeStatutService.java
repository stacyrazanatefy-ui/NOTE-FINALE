package com.example.forage.service;

import com.example.forage.model.Demande;
import com.example.forage.model.DemandeStatut;
import com.example.forage.model.Statut;
import com.example.forage.repository.DemandeStatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeStatutService {
    
    @Autowired
    private DemandeStatutRepository demandeStatutRepository;
    
    @Autowired
    private StatutService statutService;
    
    /**
     * Ajouter un statut à une demande
     */
    public DemandeStatut addStatutToDemande(Demande demande, String libelleStatut) {
        Optional<Statut> statutOpt = statutService.getStatutByLibelle(libelleStatut);
        if (statutOpt.isEmpty()) {
            throw new RuntimeException("Statut non trouvé: " + libelleStatut);
        }
        
        DemandeStatut demandeStatut = new DemandeStatut(demande, statutOpt.get(), LocalDateTime.now());
        return demandeStatutRepository.save(demandeStatut);
    }
    
    /**
     * Ajouter le statut "DEMANDE_CREEE" automatiquement à la création d'une demande
     */
    public DemandeStatut addStatutInitial(Demande demande) {
        return addStatutToDemande(demande, "DEMANDE_CREEE");
    }
    
    /**
     * Récupérer l'historique des statuts d'une demande (trié par date décroissante)
     */
    public List<DemandeStatut> getHistoriqueStatutsByDemandeId(Long demandeId) {
        return demandeStatutRepository.findByDemandeIdOrderByDateDesc(demandeId);
    }
    
    /**
     * Récupérer l'historique des statuts d'une demande (trié par date croissante)
     */
    public List<DemandeStatut> getHistoriqueChronologiqueByDemandeId(Long demandeId) {
        return demandeStatutRepository.findByDemandeIdOrderByDateAsc(demandeId);
    }
    
    /**
     * Récupérer le dernier statut d'une demande
     */
    public Optional<DemandeStatut> getLastStatutByDemandeId(Long demandeId) {
        DemandeStatut lastStatut = demandeStatutRepository.findLastStatutByDemandeId(demandeId);
        return Optional.ofNullable(lastStatut);
    }
    
    /**
     * Compter le nombre de changements de statut pour une demande
     */
    public long countStatutChangesByDemandeId(Long demandeId) {
        return demandeStatutRepository.countByDemandeId(demandeId);
    }
    
    /**
     * Supprimer tous les statuts d'une demande
     */
    public void deleteStatutsByDemandeId(Long demandeId) {
        List<DemandeStatut> statuts = demandeStatutRepository.findByDemandeIdOrderByDateDesc(demandeId);
        demandeStatutRepository.deleteAll(statuts);
    }
    
    /**
     * Changer le statut d'une demande
     */
    public DemandeStatut changerStatutDemande(Long demandeId, String nouveauLibelleStatut) {
        Optional<Statut> statutOpt = statutService.getStatutByLibelle(nouveauLibelleStatut);
        if (statutOpt.isEmpty()) {
            throw new RuntimeException("Statut non trouvé: " + nouveauLibelleStatut);
        }
        
        Demande demande = new Demande();
        demande.setId(demandeId);
        
        DemandeStatut demandeStatut = new DemandeStatut(demande, statutOpt.get(), LocalDateTime.now());
        return demandeStatutRepository.save(demandeStatut);
    }
}
