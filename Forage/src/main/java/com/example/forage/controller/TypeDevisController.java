package com.example.forage.controller;

import com.example.forage.model.TypeDevis;
import com.example.forage.service.TypeDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/types-devis")
public class TypeDevisController {

    @Autowired
    private TypeDevisService typeDevisService;

    @GetMapping
    public String listTypesDevis(Model model) {
        List<TypeDevis> typesDevis = typeDevisService.getAllTypesDevis();
        model.addAttribute("typesDevis", typesDevis);
        model.addAttribute("title", "Types de Devis");
        return "types-devis/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("title", "Ajouter un Type de Devis");
        model.addAttribute("action", "/types-devis/save");
        return "types-devis/form";
    }
    
    @PostMapping("/save")
    public String saveTypeDevis(@RequestParam String libelle, 
                               @RequestParam(required = false) String description,
                               RedirectAttributes redirectAttributes) {
        try {
            typeDevisService.addTypeDevis(libelle, description);
            redirectAttributes.addFlashAttribute("success", "Type de devis ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du type de devis: " + e.getMessage());
        }
        return "redirect:/types-devis";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TypeDevis typeDevis = typeDevisService.getTypeDevisById(id);
            if (typeDevis == null) {
                redirectAttributes.addFlashAttribute("error", "Type de devis non trouvé");
                return "redirect:/types-devis";
            }
            
            model.addAttribute("typeDevis", typeDevis);
            model.addAttribute("title", "Modifier un Type de Devis");
            model.addAttribute("action", "/types-devis/update");
            return "types-devis/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Type de devis non trouvé");
            return "redirect:/types-devis";
        }
    }
    
    @PostMapping("/update")
    public String updateTypeDevis(@RequestParam Long id,
                               @RequestParam String libelle,
                               @RequestParam(required = false) String description,
                               RedirectAttributes redirectAttributes) {
        try {
            typeDevisService.updateTypeDevis(id, libelle, description);
            redirectAttributes.addFlashAttribute("success", "Type de devis modifié avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/types-devis";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteTypeDevis(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            typeDevisService.deleteTypeDevis(id);
            redirectAttributes.addFlashAttribute("success", "Type de devis supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/types-devis";
    }
}
