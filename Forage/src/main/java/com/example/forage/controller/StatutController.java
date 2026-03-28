package com.example.forage.controller;

import com.example.forage.model.DemandeStatut;
import com.example.forage.model.Statut;
import com.example.forage.service.StatutService;
import com.example.forage.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/statuts")
public class StatutController {
    
    @Autowired
    private StatutService statutService;
    
    @Autowired
    private DemandeService demandeService;
    
    /**
     * Affiche la liste de tous les statuts
     */
    @GetMapping
    public String listStatuts(Model model) {
        List<Statut> statuts = statutService.getAllStatuts();
        model.addAttribute("statuts", statuts);
        return "statut/list";
    }
    
    /**
     * Affiche le formulaire d'ajout d'un statut
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("statut", new Statut());
        model.addAttribute("title", "Ajouter un statut");
        return "statut/form";
    }
    
    /**
     * Enregistre un nouveau statut
     */
    @PostMapping("/save")
    public String saveStatut(@ModelAttribute Statut statut, RedirectAttributes redirectAttributes) {
        try {
            if (statutService.existsByLibelle(statut.getLibelle())) {
                redirectAttributes.addFlashAttribute("error", "Ce libellé de statut existe déjà !");
                return "redirect:/statuts/new";
            }
            
            statutService.saveStatut(statut);
            redirectAttributes.addFlashAttribute("success", "Statut ajouté avec succès !");
            return "redirect:/statuts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du statut : " + e.getMessage());
            return "redirect:/statuts/new";
        }
    }
    
    /**
     * Affiche le formulaire de modification d'un statut
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Statut> statut = statutService.getStatutById(id);
        if (statut.isPresent()) {
            model.addAttribute("statut", statut.get());
            model.addAttribute("title", "Modifier un statut");
            return "statut/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Statut non trouvé !");
            return "redirect:/statuts";
        }
    }
    
    /**
     * Met à jour un statut
     */
    @PostMapping("/update")
    public String updateStatut(@ModelAttribute Statut statut, RedirectAttributes redirectAttributes) {
        try {
            Statut existingStatut = statutService.getStatutById(statut.getId()).orElse(null);
            if (existingStatut == null) {
                redirectAttributes.addFlashAttribute("error", "Statut non trouvé !");
                return "redirect:/statuts";
            }
            
            // Vérifier si le libellé est utilisé par un autre statut
            if (!existingStatut.getLibelle().equals(statut.getLibelle()) && 
                statutService.existsByLibelle(statut.getLibelle())) {
                redirectAttributes.addFlashAttribute("error", "Ce libellé de statut existe déjà !");
                return "redirect:/statuts/edit/" + statut.getId();
            }
            
            statutService.saveStatut(statut);
            redirectAttributes.addFlashAttribute("success", "Statut modifié avec succès !");
            return "redirect:/statuts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification du statut : " + e.getMessage());
            return "redirect:/statuts/edit/" + statut.getId();
        }
    }
    
    /**
     * Supprime un statut
     */
    @GetMapping("/delete/{id}")
    public String deleteStatut(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            statutService.deleteStatut(id);
            redirectAttributes.addFlashAttribute("success", "Statut supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du statut : " + e.getMessage());
        }
        return "redirect:/statuts";
    }
    
    /**
     * Affiche l'historique des statuts d'une demande
     */
    @GetMapping("/historique/{demandeId}")
    public String showHistorique(@PathVariable Long demandeId, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<DemandeStatut> historique = demandeService.getHistoriqueStatuts(demandeId);
            model.addAttribute("historique", historique);
            model.addAttribute("demandeId", demandeId);
            return "statut/historique";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la récupération de l'historique : " + e.getMessage());
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
            redirectAttributes.addFlashAttribute("success", "Statut changé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du changement de statut : " + e.getMessage());
        }
        return "redirect:/demandes";
    }
}
