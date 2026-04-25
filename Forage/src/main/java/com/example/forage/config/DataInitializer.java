package com.example.forage.config;

import com.example.forage.service.StatutService;
import com.example.forage.service.StatutDevisService;
import com.example.forage.service.TypeDevisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private StatutService statutService;
    
    @Autowired
    private TypeDevisService typeDevisService;
    
    @Autowired
    private StatutDevisService statutDevisService;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Initialisation des données par défaut...");
        
        try {
            // Initialiser les statuts par défaut
            statutService.initializeDefaultStatuts();
            
            // Initialiser les types de devis par défaut
            typeDevisService.initializeDefaultTypesDevis();
            
            // Initialiser les statuts de devis par défaut
            statutDevisService.initializeDefaultStatuts();
            
            logger.info("Initialisation des données terminée avec succès !");
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation des données: {}", e.getMessage(), e);
        }
    }
}
