package com.example.forage.service;

import com.example.forage.model.Devis;
import com.example.forage.model.DevisDetails;
import com.example.forage.model.Demande;
import com.example.forage.repository.DevisRepository;
import com.example.forage.repository.DevisDetailsRepository;
import com.example.forage.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DevisService {
    
    @Autowired
    private DevisRepository devisRepository;
    
    @Autowired
    private DevisDetailsRepository devisDetailsRepository;
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    /**
     * Récupérer tous les devis avec demande et client
     */
    public List<Devis> getAllDevisWithDemandeAndClient() {
        return devisRepository.findAllWithDemandeAndClient();
    }
    
    /**
     * Récupérer un devis par son ID avec tous les détails
     */
    public Optional<Devis> getDevisByIdWithDetails(Long id) {
        return devisRepository.findByIdWithDetails(id);
    }
    
    /**
     * Récupérer un devis par son ID avec demande
     */
    public Optional<Devis> getDevisByIdWithDemande(Long id) {
        return devisRepository.findByIdWithDemande(id);
    }
    
    /**
     * Enregistrer un devis avec ses détails (transactionnel)
     */
    @Transactional
    public Devis saveDevisWithDetails(Devis devis, List<DevisDetails> details) {
        try {
            // 1. Sauvegarder le devis principal
            Devis savedDevis = devisRepository.save(devis);
            
            // 2. Calculer et mettre à jour le montant total
            Double total = 0.0;
            for (DevisDetails detail : details) {
                detail.setDevis(savedDevis);
                detail.updateMontant(); // Recalculer le montant
                total += detail.getMontant();
            }
            
            // 3. Sauvegarder tous les détails
            List<DevisDetails> savedDetails = devisDetailsRepository.saveAll(details);
            
            // 4. Mettre à jour le montant total du devis
            savedDevis.setMontantTotal(total);
            savedDevis.setDetails(savedDetails);
            
            return devisRepository.save(savedDevis);
            
        } catch (Exception e) {
            // En cas d'erreur, la transaction sera rollback automatiquement
            throw new RuntimeException("Erreur lors de l'enregistrement du devis: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mettre à jour un devis
     */
    @Transactional
    public Devis updateDevis(Long id, Devis devis) {
        Optional<Devis> existingDevisOpt = devisRepository.findByIdWithDetails(id);
        if (existingDevisOpt.isPresent()) {
            Devis existingDevis = existingDevisOpt.get();
            
            // Mettre à jour les champs autorisés
            existingDevis.setTypeDevis(devis.getTypeDevis());
            existingDevis.setStatut(devis.getStatut());
            
            return devisRepository.save(existingDevis);
        }
        return null;
    }
    
    /**
     * Mettre à jour le statut d'un devis
     */
    @Transactional
    public Devis updateStatutDevis(Long id, Devis.StatutDevis nouveauStatut) {
        Optional<Devis> devisOpt = devisRepository.findById(id);
        if (devisOpt.isPresent()) {
            Devis devis = devisOpt.get();
            devis.setStatut(nouveauStatut);
            return devisRepository.save(devis);
        }
        return null;
    }
    
    /**
     * Supprimer un devis et ses détails
     */
    @Transactional
    public boolean deleteDevis(Long id) {
        try {
            Optional<Devis> devisOpt = devisRepository.findById(id);
            if (devisOpt.isPresent()) {
                // Les détails seront supprimés automatiquement grâce à cascade
                devisRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du devis: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lister les devis par statut
     */
    public List<Devis> getDevisByStatut(Devis.StatutDevis statut) {
        return devisRepository.findByStatutOrderByDateCreationDesc(statut);
    }
    
    /**
     * Lister les devis par type
     */
    public List<Devis> getDevisByType(String typeDevis) {
        return devisRepository.findByTypeDevisOrderByDateCreationDesc(typeDevis);
    }
    
    /**
     * Lister les devis par demande
     */
    public List<Devis> getDevisByDemande(Long demandeId) {
        return devisRepository.findByDemandeIdOrderByDateCreationDesc(demandeId);
    }
    
    /**
     * Lister les devis par client
     */
    public List<Devis> getDevisByClient(Long clientId) {
        return devisRepository.findByClientIdOrderByDateCreationDesc(clientId);
    }
    
    /**
     * Rechercher des devis par période
     */
    public List<Devis> getDevisByPeriode(LocalDateTime startDate, LocalDateTime endDate) {
        return devisRepository.findByDateCreationBetweenOrderByDateCreationDesc(startDate, endDate);
    }
    
    /**
     * Vérifier si un devis existe pour une demande et un type
     */
    public boolean existsDevisForDemandeAndType(Long demandeId, String typeDevis) {
        return devisRepository.existsByDemandeIdAndTypeDevis(demandeId, typeDevis);
    }
    
    /**
     * Récupérer le dernier devis d'une demande
     */
    public Optional<Devis> getLastDevisByDemande(Long demandeId) {
        return devisRepository.findFirstByDemandeIdOrderByDateCreationDesc(demandeId);
    }
    
    /**
     * Récupérer les détails d'un devis
     */
    public List<DevisDetails> getDetailsByDevis(Long devisId) {
        return devisDetailsRepository.findByDevisIdOrderByLibelle(devisId);
    }
    
    /**
     * Calculer le montant total d'un devis
     */
    public Double calculateTotalDevis(Long devisId) {
        return devisDetailsRepository.calculateTotalByDevisId(devisId);
    }
    
    /**
     * Ajouter un détail à un devis existant
     */
    @Transactional
    public DevisDetails addDetailToDevis(Long devisId, DevisDetails detail) {
        Optional<Devis> devisOpt = devisRepository.findById(devisId);
        if (devisOpt.isPresent()) {
            Devis devis = devisOpt.get();
            detail.setDevis(devis);
            detail.updateMontant();
            
            DevisDetails savedDetail = devisDetailsRepository.save(detail);
            
            // Mettre à jour le montant total du devis
            Double newTotal = calculateTotalDevis(devisId);
            devis.setMontantTotal(newTotal);
            devisRepository.save(devis);
            
            return savedDetail;
        }
        return null;
    }
    
    /**
     * Supprimer un détail d'un devis
     */
    @Transactional
    public boolean deleteDetail(Long detailId) {
        try {
            Optional<DevisDetails> detailOpt = devisDetailsRepository.findById(detailId);
            if (detailOpt.isPresent()) {
                DevisDetails detail = detailOpt.get();
                Long devisId = detail.getDevis().getId();
                
                // Supprimer le détail
                devisDetailsRepository.deleteById(detailId);
                
                // Mettre à jour le montant total du devis
                Double newTotal = calculateTotalDevis(devisId);
                Optional<Devis> devisOpt = devisRepository.findById(devisId);
                if (devisOpt.isPresent()) {
                    Devis devis = devisOpt.get();
                    devis.setMontantTotal(newTotal);
                    devisRepository.save(devis);
                }
                
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du détail: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtenir les statistiques des devis
     */
    public DevisStatistics getStatistics() {
        DevisStatistics stats = new DevisStatistics();
        stats.setTotalDevis(devisRepository.count());
        stats.setTotalCrees(devisRepository.countByStatut(Devis.StatutDevis.CREE));
        stats.setTotalAcceptes(devisRepository.countByStatut(Devis.StatutDevis.ACCEPTE));
        stats.setTotalRefuses(devisRepository.countByStatut(Devis.StatutDevis.REFUSE));
        stats.setTotalEtudes(devisRepository.countByTypeDevis("Etude"));
        stats.setTotalForages(devisRepository.countByTypeDevis("Forage"));
        return stats;
    }
    
    /**
     * Classe interne pour les statistiques
     */
    public static class DevisStatistics {
        private long totalDevis;
        private long totalCrees;
        private long totalAcceptes;
        private long totalRefuses;
        private long totalEtudes;
        private long totalForages;
        
        // Getters et Setters
        public long getTotalDevis() { return totalDevis; }
        public void setTotalDevis(long totalDevis) { this.totalDevis = totalDevis; }
        public long getTotalCrees() { return totalCrees; }
        public void setTotalCrees(long totalCrees) { this.totalCrees = totalCrees; }
        public long getTotalAcceptes() { return totalAcceptes; }
        public void setTotalAcceptes(long totalAcceptes) { this.totalAcceptes = totalAcceptes; }
        public long getTotalRefuses() { return totalRefuses; }
        public void setTotalRefuses(long totalRefuses) { this.totalRefuses = totalRefuses; }
        public long getTotalEtudes() { return totalEtudes; }
        public void setTotalEtudes(long totalEtudes) { this.totalEtudes = totalEtudes; }
        public long getTotalForages() { return totalForages; }
        public void setTotalForages(long totalForages) { this.totalForages = totalForages; }
    }
}
