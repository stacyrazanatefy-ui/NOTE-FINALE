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
import java.util.Set;
import java.util.HashSet;

@Service
public class DevisService {
    
    @Autowired
    private DevisRepository devisRepository;
    
    @Autowired
    private DevisDetailsRepository devisDetailsRepository;
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private StatutDevisService statutDevisService;
    
    @Autowired
    private TypeDevisService typeDevisService;
    
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
     * Obtenir le libellé de statut correct pour l'affichage
     */
    public String getStatutLibelleForDisplay(Devis devis) {
        // D'abord, essayer de trouver un statut personnalisé avec le même libellé
        java.util.List<com.example.forage.model.StatutDevis> statutsDevis = statutDevisService.getAllStatutsDevis();
        
        // Si le devis a un statut personnalisé (qu'on a stocké comme ACCEPTÉ), 
        // on doit trouver le bon libellé basé sur une logique métier
        // Pour l'instant, on retourne le libellé de l'énumération
        return devis.getStatut().getLibelle();
    }
    
    /**
     * Mettre à jour le statut d'un devis avec libellé dynamique
     */
    @Transactional
    public Devis updateStatutDevisByLibelle(Long id, String nouveauStatutLibelle) {
        Optional<Devis> devisOpt = devisRepository.findById(id);
        if (devisOpt.isPresent()) {
            Devis devis = devisOpt.get();
            
            // D'abord, chercher dans l'énumération (statuts par défaut)
            for (Devis.StatutDevis statutEnum : Devis.StatutDevis.values()) {
                if (statutEnum.getLibelle().equalsIgnoreCase(nouveauStatutLibelle)) {
                    devis.setStatut(statutEnum);
                    // Vider le libellé personnalisé pour les statuts par défaut
                    devis.setStatutPersonnaliseLibelle(null);
                    return devisRepository.save(devis);
                }
            }
            
            // Si le statut n'est pas dans l'énumération, vérifier s'il existe dans la base
            java.util.List<com.example.forage.model.StatutDevis> statutsDevis = statutDevisService.getAllStatutsDevis();
            boolean statutExists = statutsDevis.stream()
                    .anyMatch(statut -> statut.getLibelle().equalsIgnoreCase(nouveauStatutLibelle));
            
            if (statutExists) {
                // Pour les statuts personnalisés, stocker le libellé personnalisé
                devis.setStatutPersonnaliseLibelle(nouveauStatutLibelle);
                // Utiliser un statut par défaut pour la compatibilité
                devis.setStatut(Devis.StatutDevis.ACCEPTE);
                return devisRepository.save(devis);
            } else {
                // Si le statut n'existe même pas dans la base, utiliser CREE
                devis.setStatut(Devis.StatutDevis.CREE);
                devis.setStatutPersonnaliseLibelle(null);
                return devisRepository.save(devis);
            }
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
     * Calculer le chiffre d'affaires prévisionnel total
     */
    public Double getChiffreAffairesPrevisionnel() {
        return devisRepository.calculateChiffreAffairesPrevisionnel();
    }
    
    /**
     * Calculer le chiffre d'affaires prévisionnel par statut
     */
    public Double getChiffreAffairesPrevisionnelByStatut(Devis.StatutDevis statut) {
        return devisRepository.calculateChiffreAffairesPrevisionnelByStatut(statut);
    }
    
    /**
     * Calculer le chiffre d'affaires prévisionnel par type
     */
    public Double getChiffreAffairesPrevisionnelByType(String typeDevis) {
        return devisRepository.calculateChiffreAffairesPrevisionnelByType(typeDevis);
    }
    
    /**
     * Calculer le chiffre d'affaires prévisionnel par période
     */
    public Double getChiffreAffairesPrevisionnelByPeriode(LocalDateTime startDate, LocalDateTime endDate) {
        return devisRepository.calculateChiffreAffairesPrevisionnelByPeriod(startDate, endDate);
    }
    
    /**
     * Récupérer tous les types de devis disponibles
     */
    public Set<String> getAllTypesDevis() {
        List<Devis> allDevis = devisRepository.findAll();
        Set<String> types = new HashSet<>();
        
        for (Devis devis : allDevis) {
            if (devis.getTypeDevis() != null && !devis.getTypeDevis().trim().isEmpty()) {
                types.add(devis.getTypeDevis());
            }
        }
        
        // Ajouter les types par défaut si aucun n'existe
        if (types.isEmpty()) {
            types.add("ETUDE");
            types.add("FORAGE");
        }
        
        return types;
    }
    
    /**
     * Ajouter un nouveau type de devis
     */
    public void addTypeDevis(String typeDevis) {
        if (typeDevis == null || typeDevis.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de devis ne peut pas être vide");
        }
        
        // Vérifier si le type existe déjà
        Set<String> existingTypes = getAllTypesDevis();
        if (existingTypes.contains(typeDevis.trim().toUpperCase())) {
            throw new IllegalArgumentException("Ce type de devis existe déjà");
        }
        
        // Le type sera ajouté automatiquement lorsqu'un devis sera créé avec ce type
        // Pour l'instant, on ne fait rien de spécial car les types sont dynamiques
    }
    
    /**
     * Mettre à jour un type de devis
     */
    public void updateTypeDevis(String oldType, String newType) {
        if (oldType == null || oldType.trim().isEmpty() || newType == null || newType.trim().isEmpty()) {
            throw new IllegalArgumentException("Les types de devis ne peuvent pas être vides");
        }
        
        // Mettre à jour tous les devis qui ont l'ancien type
        List<Devis> devisToUpdate = devisRepository.findByTypeDevis(oldType);
        for (Devis devis : devisToUpdate) {
            devis.setTypeDevis(newType.trim().toUpperCase());
            devisRepository.save(devis);
        }
    }
    
    /**
     * Supprimer un type de devis
     */
    public void deleteTypeDevis(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de devis ne peut pas être vide");
        }
        
        // Vérifier si des devis utilisent ce type
        List<Devis> devisWithThisType = devisRepository.findByTypeDevis(type);
        if (!devisWithThisType.isEmpty()) {
            throw new IllegalArgumentException("Impossible de supprimer ce type car il est utilisé par " + devisWithThisType.size() + " devis");
        }
        
        // Comme les types sont dynamiques, pas besoin de suppression explicite
        // Le type disparaîtra automatiquement s'il n'est plus utilisé
    }
    
    /**
     * Obtenir les statistiques des devis
     */
    public DevisStatistics getStatistics() {
        DevisStatistics stats = new DevisStatistics();
        stats.setTotalDevis(devisRepository.count());
        stats.setTotalCrees(devisRepository.countByStatut(Devis.StatutDevis.CREE));
        stats.setTotalAcceptes(devisRepository.countByStatut(Devis.StatutDevis.ACCEPTE));
        stats.setTotalRejetes(devisRepository.countByStatut(Devis.StatutDevis.REFUSE));
        stats.setTotalEtudes(devisRepository.countByTypeDevis("ETUDE"));
        stats.setTotalForages(devisRepository.countByTypeDevis("FORAGE"));
        
        // Calculer les chiffres d'affaires
        stats.setChiffreAffairesTotal(devisRepository.calculateChiffreAffairesPrevisionnel());
        stats.setChiffreAffairesCrees(devisRepository.calculateChiffreAffairesPrevisionnelByStatut(Devis.StatutDevis.CREE));
        stats.setChiffreAffairesAcceptes(devisRepository.calculateChiffreAffairesPrevisionnelByStatut(Devis.StatutDevis.ACCEPTE));
        stats.setChiffreAffairesRejetes(devisRepository.calculateChiffreAffairesPrevisionnelByStatut(Devis.StatutDevis.REFUSE));
        stats.setChiffreAffairesEtudes(devisRepository.calculateChiffreAffairesPrevisionnelByType("ETUDE"));
        stats.setChiffreAffairesForages(devisRepository.calculateChiffreAffairesPrevisionnelByType("FORAGE"));
        
        return stats;
    }
    
    /**
     * Récupérer un client par son ID
     */
    public Optional<com.example.forage.model.Client> getClientById(Long clientId) {
        // Chercher un devis qui appartient à ce client pour récupérer les infos du client
        List<Devis> devis = devisRepository.findByClientIdOrderByDateCreationDesc(clientId);
        if (devis.isEmpty()) {
            return Optional.empty();
        }
        // Récupérer le client depuis le premier devis trouvé
        return Optional.of(devis.get(0).getDemande().getClient());
    }
    
    /**
     * Mettre à jour un devis avec ses détails
     */
    @Transactional
    public void updateDevisWithDetails(Devis devis, Long[] detailsIds, String[] detailsDeleted, String[] libelles, Double[] prixUnitaires, Integer[] quantites) {
        System.out.println("=== MISE À JOUR DEVIS ID: " + devis.getId() + " ===");
        
        // 1. Supprimer TOUS les détails existants
        List<DevisDetails> existingDetails = devisDetailsRepository.findByDevisIdOrderByLibelle(devis.getId());
        System.out.println("Suppression de " + existingDetails.size() + " détails existants");
        
        for (DevisDetails detail : existingDetails) {
            System.out.println("Suppression du détail ID: " + detail.getId());
            devisDetailsRepository.delete(detail);
        }
        devisDetailsRepository.flush();
        
        // 2. Recréer uniquement les détails non supprimés
        Double total = 0.0;
        for (int i = 0; i < libelles.length; i++) {
            // Ignorer les lignes marquées comme supprimées
            if (detailsDeleted != null && i < detailsDeleted.length && "true".equals(detailsDeleted[i])) {
                System.out.println("Ignorée ligne supprimée " + i);
                continue;
            }
            
            if (libelles[i] != null && !libelles[i].trim().isEmpty() && 
                prixUnitaires[i] != null && prixUnitaires[i] > 0 && 
                quantites[i] != null && quantites[i] > 0) {
                
                // Appliquer la réduction si nécessaire
                Double prixUnitaire = prixUnitaires[i];
                if (prixUnitaire >= 1000000) {
                    prixUnitaire = prixUnitaire * 0.9; // 10% de réduction
                }
                
                Double montant = prixUnitaire * quantites[i];
                total += montant;
                
                // Créer un nouveau détail
                DevisDetails detail = new DevisDetails();
                detail.setDevis(devis);
                detail.setLibelle(libelles[i]);
                detail.setPrixUnitaire(prixUnitaire);
                detail.setQuantite(quantites[i]);
                detail.setMontant(montant);
                
                DevisDetails savedDetail = devisDetailsRepository.save(detail);
                System.out.println("Créé nouveau détail ID: " + savedDetail.getId() + " - " + libelles[i]);
            }
        }
        
        // 3. Mettre à jour le total du devis
        devis.setMontantTotal(total);
        devisRepository.save(devis);
        System.out.println("Total mis à jour: " + total);
        System.out.println("=== FIN MISE À JOUR DEVIS ===");
    }
    
    /**
     * Classe interne pour les statistiques
     */
    public static class DevisStatistics {
        private long totalDevis;
        private long totalCrees;
        private long totalAcceptes;
        private long totalRejetes;
        private long totalEtudes;
        private long totalForages;
        
        // Chiffre d'affaires prévisionnel
        private Double chiffreAffairesTotal;
        private Double chiffreAffairesCrees;
        private Double chiffreAffairesAcceptes;
        private Double chiffreAffairesRejetes;
        private Double chiffreAffairesEtudes;
        private Double chiffreAffairesForages;
        
        // Getters et Setters pour les compteurs
        public long getTotalDevis() { return totalDevis; }
        public void setTotalDevis(long totalDevis) { this.totalDevis = totalDevis; }
        public long getTotalCrees() { return totalCrees; }
        public void setTotalCrees(long totalCrees) { this.totalCrees = totalCrees; }
        public long getTotalAcceptes() { return totalAcceptes; }
        public void setTotalAcceptes(long totalAcceptes) { this.totalAcceptes = totalAcceptes; }
        public long getTotalRejetes() { return totalRejetes; }
        public void setTotalRejetes(long totalRejetes) { this.totalRejetes = totalRejetes; }
        public long getTotalEtudes() { return totalEtudes; }
        public void setTotalEtudes(long totalEtudes) { this.totalEtudes = totalEtudes; }
        public long getTotalForages() { return totalForages; }
        public void setTotalForages(long totalForages) { this.totalForages = totalForages; }
        
        // Getters et Setters pour le chiffre d'affaires
        public Double getChiffreAffairesTotal() { return chiffreAffairesTotal; }
        public void setChiffreAffairesTotal(Double chiffreAffairesTotal) { this.chiffreAffairesTotal = chiffreAffairesTotal; }
        public Double getChiffreAffairesCrees() { return chiffreAffairesCrees; }
        public void setChiffreAffairesCrees(Double chiffreAffairesCrees) { this.chiffreAffairesCrees = chiffreAffairesCrees; }
        public Double getChiffreAffairesAcceptes() { return chiffreAffairesAcceptes; }
        public void setChiffreAffairesAcceptes(Double chiffreAffairesAcceptes) { this.chiffreAffairesAcceptes = chiffreAffairesAcceptes; }
        public Double getChiffreAffairesRejetes() { return chiffreAffairesRejetes; }
        public void setChiffreAffairesRejetes(Double chiffreAffairesRejetes) { this.chiffreAffairesRejetes = chiffreAffairesRejetes; }
        public Double getChiffreAffairesEtudes() { return chiffreAffairesEtudes; }
        public void setChiffreAffairesEtudes(Double chiffreAffairesEtudes) { this.chiffreAffairesEtudes = chiffreAffairesEtudes; }
        public Double getChiffreAffairesForages() { return chiffreAffairesForages; }
        public void setChiffreAffairesForages(Double chiffreAffairesForages) { this.chiffreAffairesForages = chiffreAffairesForages; }
    }
    
    /**
     * Recherche des devis par lieu (via la demande associée)
     */
    public List<Devis> searchDevisByLieu(String lieu) {
        return devisRepository.findByDemande_LieuContainingIgnoreCase(lieu);
    }
    
    /**
     * Récupère les devis par statut (libellé dynamique)
     */
    public List<Devis> getDevisByStatut(String statutLibelle) {
        try {
            // Essayer de convertir en énumération d'abord (compatibilité)
            Devis.StatutDevis statutEnum = Devis.StatutDevis.valueOf(statutLibelle.toUpperCase());
            return devisRepository.findByStatutOrderByDateCreationDesc(statutEnum);
        } catch (IllegalArgumentException e) {
            // Si ce n'est pas une énumération valide, filtrer par libellé
            List<Devis> allDevis = getAllDevisWithDemandeAndClient();
            return allDevis.stream()
                    .filter(devis -> devis.getStatut().getLibelle().equalsIgnoreCase(statutLibelle))
                    .collect(java.util.stream.Collectors.toList());
        }
    }
}
