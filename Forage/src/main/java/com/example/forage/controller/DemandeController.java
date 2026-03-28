package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.model.DemandeStatut;
import com.example.forage.model.Statut;
import com.example.forage.service.ClientService;
import com.example.forage.service.DemandeService;
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
    private StatutService statutService;
    
    /**
     * Affiche la liste de toutes les demandes avec leur dernier statut
     */
    @GetMapping
    public String listDemandes(Model model) {
        List<Demande> demandes = demandeService.getAllDemandesWithClient();
        
        // Créer une map pour stocker les derniers statuts
        java.util.Map<Long, DemandeStatut> lastStatuts = new java.util.HashMap<>();
        for (Demande demande : demandes) {
            Optional<DemandeStatut> lastStatut = demandeService.getLastStatut(demande.getId());
            lastStatut.ifPresent(demandeStatut -> lastStatuts.put(demande.getId(), demandeStatut));
        }
        
        model.addAttribute("demandes", demandes);
        model.addAttribute("lastStatuts", lastStatuts);
        model.addAttribute("title", "Liste des Demandes");
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
     * Affiche l'historique des statuts d'une demande
     */
    @GetMapping("/historique/{id}")
    public String showHistorique(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Demande> demandeOpt = demandeService.getDemandeById(id);
        if (demandeOpt.isPresent()) {
            List<DemandeStatut> historique = demandeService.getHistoriqueStatuts(id);
            model.addAttribute("demande", demandeOpt.get());
            model.addAttribute("historique", historique);
            model.addAttribute("title", "Historique des statuts - Demande #" + id);
            return "demande/historique";
        } else {
            redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
            return "redirect:/demandes";
        }
    }
    
    /**
     * Change le statut d'une demande
     */
    @PostMapping("/changer-statut")
    public String changerStatut(@RequestParam Long demandeId, 
                               @RequestParam String nouveauStatut,
                               RedirectAttributes redirectAttributes) {
        try {
            demandeService.changerStatutDemande(demandeId, nouveauStatut);
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
        Optional<Client> client = clientService.getClientById(clientId);
        if (client.isPresent()) {
            List<Demande> demandes = demandeService.getDemandesByClientId(clientId);
            model.addAttribute("demandes", demandes);
            model.addAttribute("client", client.get());
            model.addAttribute("title", "Demandes du client: " + client.get().getNom());
            return "demande/list";
        } else {
            return "redirect:/demandes";
        }
    }
}
