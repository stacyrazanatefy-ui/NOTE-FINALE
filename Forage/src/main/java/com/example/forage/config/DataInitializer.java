package com.example.forage.config;

import com.example.forage.service.StatutService;
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
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Initialisation des données par défaut...");
        
        try {
            // Initialiser les statuts par défaut
            statutService.initializeDefaultStatuts();
            
            logger.info("Initialisation des données terminée avec succès !");
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation des données: {}", e.getMessage(), e);
        }
    }
}
