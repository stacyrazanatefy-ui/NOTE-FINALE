package com.example.forage.controller;

import com.example.forage.service.ClientService;
import com.example.forage.service.DemandeService;
import com.example.forage.service.DemandeStatutService;
import com.example.forage.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private DemandeStatutService demandeStatutService;
    
    @Autowired
    private DevisService devisService;
    
    @GetMapping("/api/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("clients", clientService.countClients());
        stats.put("demandes", demandeService.countDemandes());
        stats.put("demandesConfirmees", demandeStatutService.countDemandesByStatut("DEMANDE_CONFIRMEE"));
        stats.put("demandesAnnulees", demandeStatutService.countDemandesByStatut("DEMANDE_ANNULEE"));
        return stats;
    }
}
