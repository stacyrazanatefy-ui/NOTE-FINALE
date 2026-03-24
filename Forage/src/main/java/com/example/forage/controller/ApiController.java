package com.example.forage.controller;

import com.example.forage.service.ClientService;
import com.example.forage.service.DemandeService;
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
    
    @GetMapping("/api/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("clients", clientService.countClients());
        stats.put("demandes", demandeService.countDemandes());
        return stats;
    }
}
