package com.example.forage.controller;

import com.example.forage.model.Devis;
import com.example.forage.model.DevisDetails;
import com.example.forage.model.Demande;
import com.example.forage.service.DevisService;
import com.example.forage.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/devis")
public class DevisController {
    
    @Autowired
    private DevisService devisService;
    
    @Autowired
    private DemandeService demandeService;
    
    /**
     * Affiche la liste de tous les devis
     */
    @GetMapping
    public String listDevis(Model model) {
        List<Devis> devis = devisService.getAllDevisWithDemandeAndClient();
        model.addAttribute("devis", devis);
        model.addAttribute("title", "Liste des Devis");
        model.addAttribute("statistics", devisService.getStatistics());
        return "devis/list";
    }
    
    /**
     * Affiche le formulaire d'ajout d'un devis
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("devis", new Devis());
        model.addAttribute("title", "Ajouter un Devis");
        return "devis/form";
    }
    
    /**
     * API AJAX pour récupérer les détails d'une demande
     */
    @GetMapping("/api/demande/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDemandeDetails(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Demande> demandeOpt = demandeService.getDemandeById(id);
            if (demandeOpt.isPresent()) {
                Demande demande = demandeOpt.get();
                response.put("success", true);
                response.put("demande", Map.of(
                    "id", demande.getId(),
                    "date", demande.getDate().toString(),
                    "lieu", demande.getLieu(),
                    "district", demande.getDistrict(),
                    "client", demande.getClient() != null ? 
                        Map.of("id", demande.getClient().getId(), "nom", demande.getClient().getNom()) : null
                ));
            } else {
                response.put("success", false);
                response.put("message", "Demande non trouvée");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API AJAX pour rechercher des demandes par ID ou partie d'ID
     */
    @GetMapping("/api/demandes/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchDemandes(@RequestParam String term) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Essayer de parser comme ID
            try {
                Long id = Long.parseLong(term);
                Optional<Demande> demandeOpt = demandeService.getDemandeById(id);
                if (demandeOpt.isPresent()) {
                    Demande demande = demandeOpt.get();
                    response.put("success", true);
                    response.put("demandes", List.of(Map.of(
                        "id", demande.getId(),
                        "libelle", "Demande #" + demande.getId() + " - " + demande.getLieu(),
                        "date", demande.getDate().toString(),
                        "lieu", demande.getLieu(),
                        "district", demande.getDistrict(),
                        "client", demande.getClient() != null ? demande.getClient().getNom() : "Non défini"
                    )));
                } else {
                    response.put("success", false);
                    response.put("message", "Aucune demande trouvée");
                }
            } catch (NumberFormatException e) {
                // Si ce n'est pas un nombre, retourner vide
                response.put("success", false);
                response.put("message", "Veuillez entrer un ID numérique");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Enregistre un nouveau devis avec ses détails
     */
    @PostMapping("/save")
    public String saveDevis(
            @RequestParam Long demandeId,
            @RequestParam String typeDevis,
            @RequestParam("details_libelle[]") String[] libelles,
            @RequestParam("details_prixUnitaire[]") Double[] prixUnitaires,
            @RequestParam("details_quantite[]") Integer[] quantites,
            RedirectAttributes redirectAttributes) {
        
        try {
            // 1. Récupérer la demande
            Optional<Demande> demandeOpt = demandeService.getDemandeById(demandeId);
            if (!demandeOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Demande non trouvée");
                return "redirect:/devis/new";
            }
            
            Demande demande = demandeOpt.get();
            
            // 2. Vérifier qu'il n'existe pas déjà un devis de ce type pour cette demande
            if (devisService.existsDevisForDemandeAndType(demandeId, typeDevis)) {
                redirectAttributes.addFlashAttribute("error", "Un devis de type '" + typeDevis + "' existe déjà pour cette demande");
                return "redirect:/devis/new";
            }
            
            // 3. Créer le devis
            Devis devis = new Devis(demande, typeDevis, 0.0);
            
            // 4. Créer les détails
            java.util.List<DevisDetails> details = new java.util.ArrayList<>();
            Double total = 0.0;
            
            for (int i = 0; i < libelles.length; i++) {
                if (libelles[i] != null && !libelles[i].trim().isEmpty() && 
                    prixUnitaires[i] != null && prixUnitaires[i] > 0 && 
                    quantites[i] != null && quantites[i] > 0) {
                    
                    DevisDetails detail = new DevisDetails();
                    detail.setLibelle(libelles[i].trim());
                    detail.setPrixUnitaire(prixUnitaires[i]);
                    detail.setQuantite(quantites[i]);
                    detail.updateMontant();
                    details.add(detail);
                    total += detail.getMontant();
                }
            }
            
            if (details.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Au moins un détail valide est requis");
                return "redirect:/devis/new";
            }
            
            // 5. Enregistrer le devis avec ses détails (transactionnel)
            devis.setMontantTotal(total);
            Devis savedDevis = devisService.saveDevisWithDetails(devis, details);
            
            redirectAttributes.addFlashAttribute("success", "Devis créé avec succès (ID: " + savedDevis.getId() + ")");
            return "redirect:/devis";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du devis: " + e.getMessage());
            return "redirect:/devis/new";
        }
    }
    
    /**
     * Affiche les détails d'un devis
     */
    @GetMapping("/{id}")
    public String showDevis(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Devis> devisOpt = devisService.getDevisByIdWithDetails(id);
        if (devisOpt.isPresent()) {
            model.addAttribute("devis", devisOpt.get());
            model.addAttribute("title", "Détails du Devis #" + id);
            return "devis/details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Devis non trouvé");
            return "redirect:/devis";
        }
    }
    
    /**
     * Affiche le formulaire de modification d'un devis
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Devis> devisOpt = devisService.getDevisByIdWithDetails(id);
        if (devisOpt.isPresent()) {
            model.addAttribute("devis", devisOpt.get());
            model.addAttribute("title", "Modifier un Devis");
            return "devis/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Devis non trouvé");
            return "redirect:/devis";
        }
    }
    
    /**
     * Met à jour le statut d'un devis
     */
    @PostMapping("/update-statut")
    public String updateStatut(@RequestParam Long devisId, 
                             @RequestParam String nouveauStatut,
                             RedirectAttributes redirectAttributes) {
        try {
            Devis.StatutDevis statut = Devis.StatutDevis.valueOf(nouveauStatut.toUpperCase());
            Devis updatedDevis = devisService.updateStatutDevis(devisId, statut);
            
            if (updatedDevis != null) {
                redirectAttributes.addFlashAttribute("success", "Statut mis à jour avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Devis non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
        
        return "redirect:/devis/" + devisId;
    }
    
    /**
     * Supprime un devis
     */
    @GetMapping("/delete/{id}")
    public String deleteDevis(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = devisService.deleteDevis(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Devis supprimé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Devis non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du devis: " + e.getMessage());
        }
        return "redirect:/devis";
    }
    
    /**
     * Lister les devis par statut
     */
    @GetMapping("/statut/{statut}")
    public String listDevisByStatut(@PathVariable String statut, Model model) {
        try {
            Devis.StatutDevis statutEnum = Devis.StatutDevis.valueOf(statut.toUpperCase());
            List<Devis> devis = devisService.getDevisByStatut(statutEnum);
            model.addAttribute("devis", devis);
            model.addAttribute("title", "Devis " + statutEnum.getLibelle() + "s");
            model.addAttribute("filterType", "statut");
            model.addAttribute("filterValue", statut);
            return "devis/list";
        } catch (Exception e) {
            return "redirect:/devis";
        }
    }
    
    /**
     * Lister les devis par type
     */
    @GetMapping("/type/{type}")
    public String listDevisByType(@PathVariable String type, Model model) {
        List<Devis> devis = devisService.getDevisByType(type);
        model.addAttribute("devis", devis);
        model.addAttribute("title", "Devis de type " + type);
        model.addAttribute("filterType", "type");
        model.addAttribute("filterValue", type);
        return "devis/list";
    }
    
    /**
     * Lister les devis par client
     */
    @GetMapping("/client/{clientId}")
    public String listDevisByClient(@PathVariable Long clientId, Model model) {
        List<Devis> devis = devisService.getDevisByClient(clientId);
        model.addAttribute("devis", devis);
        model.addAttribute("title", "Devis du client #" + clientId);
        model.addAttribute("filterType", "client");
        model.addAttribute("filterValue", clientId.toString());
        return "devis/list";
    }
    
    /**
     * Rechercher des devis par période
     */
    @GetMapping("/search/date")
    public String searchDevisByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        
        List<Devis> devis = devisService.getDevisByPeriode(startDate, endDate);
        model.addAttribute("devis", devis);
        model.addAttribute("title", "Devis du " + startDate + " au " + endDate);
        model.addAttribute("filterType", "date");
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "devis/list";
    }
}
