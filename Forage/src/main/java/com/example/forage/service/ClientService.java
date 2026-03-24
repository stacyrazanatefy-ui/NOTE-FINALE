package com.example.forage.service;

import com.example.forage.model.Client;
import com.example.forage.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    /**
     * Sauvegarde un client
     */
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
    
    /**
     * Récupère tous les clients
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    /**
     * Récupère un client par son ID
     */
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    /**
     * Récupère un client par son contact
     */
    public Optional<Client> getClientByContact(String contact) {
        return clientRepository.findByContact(contact);
    }
    
    /**
     * Recherche des clients par nom
     */
    public List<Client> searchClientsByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom);
    }
    
    /**
     * Met à jour un client
     */
    public Client updateClient(Long id, Client clientDetails) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setNom(clientDetails.getNom());
            client.setContact(clientDetails.getContact());
            return clientRepository.save(client);
        }
        return null;
    }
    
    /**
     * Supprime un client par son ID
     */
    public boolean deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Vérifie si un contact existe déjà
     */
    public boolean contactExists(String contact) {
        return clientRepository.existsByContact(contact);
    }
    
    /**
     * Compte le nombre de clients
     */
    public long countClients() {
        return clientRepository.countClients();
    }
    
    /**
     * Récupère tous les clients avec leurs demandes
     */
    public List<Client> getAllClientsWithDemandes() {
        return clientRepository.findAllWithDemandes();
    }
}
