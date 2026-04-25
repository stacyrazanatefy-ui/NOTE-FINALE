package com.example.forage.controller;

import com.example.forage.service.StatutDevisService;
import com.example.forage.service.DevisService;
import com.example.forage.service.TypeDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/options")
public class OptionsController {

    @Autowired
    private StatutDevisService statutDevisService;
    
    @Autowired
    private DevisService devisService;
    
    @Autowired
    private TypeDevisService typeDevisService;

    @GetMapping
    public String options(Model model) {
        // Récupérer les statistiques pour chaque section
        model.addAttribute("totalStatuts", statutDevisService.getAllStatutsDevis().size());
        model.addAttribute("totalTypesDevis", typeDevisService.getAllTypesDevis().size());
        model.addAttribute("title", "Options");
        return "options/index";
    }
}
