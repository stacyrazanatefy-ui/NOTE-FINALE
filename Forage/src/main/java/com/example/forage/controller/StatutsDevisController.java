package com.example.forage.controller;

import com.example.forage.model.StatutDevis;
import com.example.forage.service.StatutDevisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/statuts-devis")
public class StatutsDevisController {

    @Autowired
    private StatutDevisService statutDevisService;

    /**
     * Afficher la liste de tous les statuts de devis
     */
    @GetMapping
    public String listStatutsDevis(Model model) {
        model.addAttribute("title", "Statuts des Devis");
        model.addAttribute("statuts", statutDevisService.getAllStatutsDevis());
        return "statuts-devis/list";
    }

    /**
     * Afficher le formulaire d'ajout d'un statut
     */
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("title", "Ajouter un Statut de Devis");
        model.addAttribute("statutDevis", new StatutDevis());
        model.addAttribute("action", "/statuts-devis/save");
        return "statuts-devis/form";
    }

    /**
     * Afficher le formulaire de modification d'un statut
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var statutDevis = statutDevisService.getStatutDevisById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statut non trouvé avec l'ID: " + id));
        
        model.addAttribute("title", "Modifier un Statut de Devis");
        model.addAttribute("statutDevis", statutDevis);
        model.addAttribute("action", "/statuts-devis/update/" + id);
        return "statuts-devis/form";
    }

    /**
     * Enregistrer un nouveau statut
     */
    @PostMapping("/save")
    public String saveStatut(@Valid @ModelAttribute("statutDevis") StatutDevis statutDevis,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez corriger les erreurs dans le formulaire");
            return "redirect:/statuts-devis/new";
        }

        try {
            statutDevisService.createStatutDevis(statutDevis);
            redirectAttributes.addFlashAttribute("success", "Statut de devis créé avec succès");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/statuts-devis/new";
        }

        return "redirect:/statuts-devis";
    }

    /**
     * Mettre à jour un statut existant
     */
    @PostMapping("/update/{id}")
    public String updateStatut(@PathVariable Long id,
                             @Valid @ModelAttribute("statutDevis") StatutDevis statutDevis,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez corriger les erreurs dans le formulaire");
            return "redirect:/statuts-devis/edit/" + id;
        }

        try {
            statutDevis.setId(id);
            statutDevisService.updateStatutDevis(statutDevis);
            redirectAttributes.addFlashAttribute("success", "Statut de devis modifié avec succès");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/statuts-devis/edit/" + id;
        }

        return "redirect:/statuts-devis";
    }

    /**
     * Supprimer un statut
     */
    @PostMapping("/delete/{id}")
    public String deleteStatut(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            statutDevisService.deleteStatutDevis(id);
            redirectAttributes.addFlashAttribute("success", "Statut de devis supprimé avec succès");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/statuts-devis";
    }
}
