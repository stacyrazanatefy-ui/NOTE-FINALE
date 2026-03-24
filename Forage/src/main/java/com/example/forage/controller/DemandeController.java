package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.service.ClientService;
import com.example.forage.service.DemandeService;
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
    
    /**
     * Affiche la liste de toutes les demandes
     */
    @GetMapping
    public String listDemandes(Model model) {
        List<Demande> demandes = demandeService.getAllDemandesWithClient();
        model.addAttribute("demandes", demandes);
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
        
        model.addAttribute("demande", demande);
        model.addAttribute("clients", clients);
        model.addAttribute("title", "Ajouter une Demande");
        model.addAttribute("action", "/demandes/save");
        return "demande/form";
    }
    
    /**
     * Sauvegarde une nouvelle demande
     */
    @PostMapping("/save")
    public String saveDemande(@Valid @ModelAttribute("demande") Demande demande, 
                            BindingResult result, 
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Client> clients = clientService.getAllClients();
            model.addAttribute("clients", clients);
            return "demande/form";
        }
        
        demandeService.saveDemande(demande);
        redirectAttributes.addFlashAttribute("success", "Demande ajoutée avec succès");
        return "redirect:/demandes";
    }
    
    /**
     * Affiche le formulaire de modification d'une demande
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        if (demande.isPresent()) {
            List<Client> clients = clientService.getAllClients();
            model.addAttribute("demande", demande.get());
            model.addAttribute("clients", clients);
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
            model.addAttribute("clients", clients);
            return "demande/form";
        }
        
        Demande updatedDemande = demandeService.updateDemande(demande.getId(), demande);
        if (updatedDemande != null) {
            redirectAttributes.addFlashAttribute("success", "Demande modifiée avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
        }
        return "redirect:/demandes";
    }
    
    /**
     * Supprime une demande
     */
    @GetMapping("/delete/{id}")
    public String deleteDemande(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = demandeService.deleteDemande(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Demande supprimée avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
        }
        return "redirect:/demandes";
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
