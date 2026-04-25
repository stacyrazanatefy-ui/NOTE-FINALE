package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.model.DemandeStatut;
import com.example.forage.model.Statut;
import com.example.forage.service.ClientService;
import com.example.forage.service.DemandeService;
import com.example.forage.service.DemandeStatutService;
import com.example.forage.service.DevisService;
import com.example.forage.service.StatutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/demandes")
public class DemandeController {
    
    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private DevisService devisService;
    
    @Autowired
    private StatutService statutService;
    
    @Autowired
    private DemandeStatutService demandeStatutService;
    
    /**
     * Affiche la liste de toutes les demandes avec leur dernier statut
     */
    @GetMapping
    public String listDemandes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long devisId,
            Model model) {
        List<Demande> demandes;
        
        // Appliquer les filtres
        if (devisId != null) {
            // Filtrer par devis spécifique - récupérer la demande associée au devis
            Optional<com.example.forage.model.Devis> devisOpt = devisService.getDevisByIdWithDetails(devisId);
            if (devisOpt.isPresent() && devisOpt.get().getDemande() != null) {
                demandes = List.of(devisOpt.get().getDemande());
            } else {
                demandes = List.of(); // Aucune demande si le devis n'existe pas ou n'a pas de demande associée
            }
        } else if (clientId != null) {
            // Filtrer par client spécifique
            demandes = demandeService.getDemandesByClientId(clientId);
        } else if (search != null && !search.trim().isEmpty()) {
            demandes = demandeService.searchDemandes(search.trim());
        } else if (statut != null && !statut.trim().isEmpty()) {
            demandes = demandeService.getDemandesByStatut(statut);
        } else if (lieu != null && !lieu.trim().isEmpty()) {
            demandes = demandeService.getDemandesByLieu(lieu.trim());
        } else if (district != null && !district.trim().isEmpty()) {
            demandes = demandeService.getDemandesByDistrict(district.trim());
        } else if (startDate != null || endDate != null) {
            demandes = demandeService.getDemandesByDateRange(startDate, endDate);
        } else {
            demandes = demandeService.getAllDemandesWithClient();
        }
        
        // Créer une map pour stocker les derniers statuts
        java.util.Map<Long, DemandeStatut> lastStatuts = new java.util.HashMap<>();
        for (Demande demande : demandes) {
            Optional<DemandeStatut> lastStatutOpt = demandeService.getLastStatut(demande.getId());
            lastStatutOpt.ifPresent(lastStatut -> lastStatuts.put(demande.getId(), lastStatut));
        }
        
        // Ajouter les informations du client au modèle
        if (clientId != null) {
            Optional<Client> clientOpt = clientService.getClientById(clientId);
            clientOpt.ifPresent(client -> model.addAttribute("filteredClient", client));
        }
        
        // Ajouter les informations du devis au modèle
        if (devisId != null) {
            Optional<com.example.forage.model.Devis> devisOpt = devisService.getDevisByIdWithDetails(devisId);
            devisOpt.ifPresent(devis -> model.addAttribute("filteredDevis", devis));
        }
        
        // Compter les demandes par statut
        java.util.Map<String, Long> statutCounts = new java.util.HashMap<>();
        for (DemandeStatut statutItem : lastStatuts.values()) {
            String statutLibelle = statutItem.getStatut().getLibelle();
            statutCounts.put(statutLibelle, statutCounts.getOrDefault(statutLibelle, 0L) + 1);
        }
        
        // Ajouter les attributs au modèle
        model.addAttribute("demandes", demandes);
        model.addAttribute("lastStatuts", lastStatuts);
        model.addAttribute("statutCounts", statutCounts);
        model.addAttribute("title", "Liste des Demandes");
        model.addAttribute("search", search);
        model.addAttribute("selectedStatut", statut);
        model.addAttribute("selectedLieu", lieu);
        model.addAttribute("selectedDistrict", district);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("clientId", clientId);
        
        // Ajouter les listes pour les filtres
        model.addAttribute("allLieux", demandeService.getAllDistinctLieux());
        model.addAttribute("allDistricts", demandeService.getAllDistinctDistricts());
        
        return "demande/list";
    }
    
    /**
     * Affiche le formulaire d'ajout d'une demande
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Demande demande = new Demande();
        demande.setDate(LocalDate.now());
        
        List<Client> clients = clientService.getAllClients();
        List<Statut> statuts = statutService.getAllStatuts();
        
        model.addAttribute("demande", demande);
        model.addAttribute("clients", clients);
        model.addAttribute("statuts", statuts);
        model.addAttribute("title", "Ajouter une Demande");
        model.addAttribute("action", "/demandes/save");
        return "demande/form";
    }
    
    /**
     * Sauvegarde une nouvelle demande avec statut initial automatique
     */
    @PostMapping("/save")
    public String saveDemande(@Valid @ModelAttribute("demande") Demande demande, 
                            BindingResult result, 
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Client> clients = clientService.getAllClients();
            List<Statut> statuts = statutService.getAllStatuts();
            model.addAttribute("clients", clients);
            model.addAttribute("statuts", statuts);
            return "demande/form";
        }
        
        try {
            demandeService.saveDemande(demande); // Le statut "DEMANDE_CREEE" est ajouté automatiquement
            redirectAttributes.addFlashAttribute("success", "Demande ajoutée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la demande: " + e.getMessage());
        }
        return "redirect:/demandes";
    }
    
    /**
     * Affiche le formulaire de modification d'une demande
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Demande> demandeOpt = demandeService.getDemandeById(id);
        if (demandeOpt.isPresent()) {
            List<Client> clients = clientService.getAllClients();
            List<Statut> statuts = statutService.getAllStatuts();
            
            model.addAttribute("demande", demandeOpt.get());
            model.addAttribute("clients", clients);
            model.addAttribute("statuts", statuts);
            model.addAttribute("title", "Modifier une Demande");
            model.addAttribute("action", "/demandes/update");
            return "demande/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
            return "redirect:/demandes";
        }
    }
    
    /**
     * Met à jour une demande existante
     */
    @PostMapping("/update")
    public String updateDemande(@Valid @ModelAttribute("demande") Demande demande, 
                               BindingResult result, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Client> clients = clientService.getAllClients();
            List<Statut> statuts = statutService.getAllStatuts();
            model.addAttribute("clients", clients);
            model.addAttribute("statuts", statuts);
            return "demande/form";
        }
        
        try {
            Demande updatedDemande = demandeService.updateDemande(demande.getId(), demande);
            if (updatedDemande != null) {
                redirectAttributes.addFlashAttribute("success", "Demande modifiée avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification de la demande: " + e.getMessage());
        }
        return "redirect:/demandes";
    }
    
    /**
     * Supprime une demande et son historique de statuts
     */
    @GetMapping("/delete/{id}")
    public String deleteDemande(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = demandeService.deleteDemande(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Demande supprimée avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression de la demande: " + e.getMessage());
        }
        return "redirect:/demandes";
    }
    
    /**
     * Affiche les détails d'une demande spécifique
     */
    @GetMapping("/{id}")
    public String showDemandeDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Demande> demandeOpt = demandeService.getDemandeById(id);
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            
            // Récupérer le dernier statut
            Optional<DemandeStatut> lastStatut = demandeService.getLastStatut(id);
            
            model.addAttribute("demande", demande);
            lastStatut.ifPresent(statut -> model.addAttribute("lastStatut", statut));
            model.addAttribute("title", "Détails de la demande #" + id);
            return "demande/details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
            return "redirect:/demandes";
        }
    }
    
    /**
     * Affiche l'historique des statuts d'une demande
     */
    @GetMapping("/historique/{id}")
    public String showHistorique(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Demande> demandeOpt = demandeService.getDemandeById(id);
        if (demandeOpt.isPresent()) {
            List<DemandeStatut> historique = demandeService.getHistoriqueStatuts(id);
            List<Statut> statuts = statutService.getAllStatuts();
            model.addAttribute("demande", demandeOpt.get());
            model.addAttribute("historique", historique);
            model.addAttribute("statuts", statuts);
            model.addAttribute("title", "Historique des statuts - Demande #" + id);
            return "demande/historique";
        } else {
            redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
            return "redirect:/demandes";
        }
    }
    
    /**
     * Affiche le formulaire de modification d'un historique de statut
     */
    @GetMapping("/historique/edit/{id}")
    public String showEditHistorique(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<DemandeStatut> demandeStatutOpt = demandeStatutService.getDemandeStatutById(id);
        if (demandeStatutOpt.isPresent()) {
            DemandeStatut demandeStatut = demandeStatutOpt.get();
            List<Statut> statuts = statutService.getAllStatuts();
            model.addAttribute("demandeStatut", demandeStatut);
            model.addAttribute("statuts", statuts);
            model.addAttribute("title", "Modifier l'historique - Demande #" + demandeStatut.getDemande().getId());
            return "demande/historique-edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Historique non trouvé");
            return "redirect:/demandes";
        }
    }
    
    /**
     * Met à jour un historique de statut
     */
    @PostMapping("/historique/update")
    public String updateHistorique(@RequestParam Long id,
                               @RequestParam String statutLibelle,
                               @RequestParam(required = false) String observation,
                               RedirectAttributes redirectAttributes) {
        try {
            demandeStatutService.updateDemandeStatut(id, statutLibelle, observation);
            redirectAttributes.addFlashAttribute("success", "Historique modifié avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/demandes/historique/" + demandeStatutService.getDemandeStatutById(id).get().getDemande().getId();
    }
    
    /**
     * Supprime un historique de statut
     */
    @GetMapping("/historique/delete/{id}")
    public String deleteHistorique(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Long demandeId = demandeStatutService.getDemandeStatutById(id).get().getDemande().getId();
            demandeStatutService.deleteDemandeStatut(id);
            redirectAttributes.addFlashAttribute("success", "Historique supprimé avec succès");
            return "redirect:/demandes/historique/" + demandeId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
            return "redirect:/demandes";
        }
    }
    
    /**
     * Change le statut d'une demande
     */
    @PostMapping("/changer-statut")
    public String changerStatut(@RequestParam Long demandeId, 
                               @RequestParam String nouveauStatut,
                               @RequestParam(required = false) String observation,
                               RedirectAttributes redirectAttributes) {
        try {
            demandeService.changerStatutDemande(demandeId, nouveauStatut, observation);
            redirectAttributes.addFlashAttribute("success", "Statut changé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du changement de statut: " + e.getMessage());
        }
        return "redirect:/demandes/historique/" + demandeId;
    }
    
    /**
     * Recherche des demandes par lieu
     */
    @GetMapping("/search/lieu")
    public String searchDemandesByLieu(@RequestParam String lieu, Model model) {
        List<Demande> demandes = demandeService.searchDemandesByLieu(lieu);
        model.addAttribute("demandes", demandes);
        model.addAttribute("title", "Résultats de recherche pour le lieu: " + lieu);
        model.addAttribute("searchTerm", lieu);
        model.addAttribute("searchType", "lieu");
        return "demande/list";
    }
    
    /**
     * Recherche des demandes par district
     */
    @GetMapping("/search/district")
    public String searchDemandesByDistrict(@RequestParam String district, Model model) {
        List<Demande> demandes = demandeService.searchDemandesByDistrict(district);
        model.addAttribute("demandes", demandes);
        model.addAttribute("title", "Résultats de recherche pour le district: " + district);
        model.addAttribute("searchTerm", district);
        model.addAttribute("searchType", "district");
        return "demande/list";
    }
    
    /**
     * Recherche des demandes par période de dates
     */
    @GetMapping("/search/date")
    public String searchDemandesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        List<Demande> demandes = demandeService.searchDemandesByDateRange(startDate, endDate);
        model.addAttribute("demandes", demandes);
        model.addAttribute("title", "Demandes du " + startDate + " au " + endDate);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "demande/list";
    }
    
    /**
     * Affiche les demandes d'un client spécifique
     */
    @GetMapping("/client/{clientId}")
    public String getDemandesByClient(@PathVariable Long clientId, Model model) {
        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) {
            return "redirect:/clients";
        }
        
        Client client = clientOpt.get();
        List<Demande> demandes = demandeService.getDemandesByClientId(clientId);
        
        // S'il y a des demandes, rediriger vers la première demande
        if (!demandes.isEmpty()) {
            return "redirect:/demandes/" + demandes.get(0).getId();
        }
        
        // Sinon, afficher la liste vide
        java.util.Map<Long, DemandeStatut> lastStatuts = new java.util.HashMap<>();
        for (Demande demande : demandes) {
            Optional<DemandeStatut> lastStatut = demandeService.getLastStatut(demande.getId());
            lastStatut.ifPresent(demandeStatut -> lastStatuts.put(demande.getId(), demandeStatut));
        }
        
        model.addAttribute("demandes", demandes);
        model.addAttribute("lastStatuts", lastStatuts);
        model.addAttribute("client", client);
        model.addAttribute("title", "Demandes du client: " + client.getNom());
        return "demande/list";
    }
}
