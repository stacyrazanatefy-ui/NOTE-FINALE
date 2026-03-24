package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    /**
     * Affiche la liste de tous les clients
     */
    @GetMapping
    public String listClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("title", "Liste des Clients");
        return "client/list";
    }
    
    /**
     * Affiche le formulaire d'ajout d'un client
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("title", "Ajouter un Client");
        model.addAttribute("action", "/clients/save");
        return "client/form";
    }
    
    /**
     * Sauvegarde un nouveau client
     */
    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("client") Client client, 
                           BindingResult result, 
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "client/form";
        }
        
        // Vérifier si le contact existe déjà
        if (clientService.contactExists(client.getContact())) {
            result.rejectValue("contact", "error.client", "Ce contact existe déjà");
            return "client/form";
        }
        
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("success", "Client ajouté avec succès");
        return "redirect:/clients";
    }
    
    /**
     * Affiche le formulaire de modification d'un client
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            model.addAttribute("client", client.get());
            model.addAttribute("title", "Modifier un Client");
            model.addAttribute("action", "/clients/update");
            return "client/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Client non trouvé");
            return "redirect:/clients";
        }
    }
    
    /**
     * Met à jour un client existant
     */
    @PostMapping("/update")
    public String updateClient(@Valid @ModelAttribute("client") Client client, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "client/form";
        }
        
        // Vérifier si le contact existe déjà pour un autre client
        Optional<Client> existingClient = clientService.getClientByContact(client.getContact());
        if (existingClient.isPresent() && !existingClient.get().getId().equals(client.getId())) {
            result.rejectValue("contact", "error.client", "Ce contact existe déjà");
            return "client/form";
        }
        
        Client updatedClient = clientService.updateClient(client.getId(), client);
        if (updatedClient != null) {
            redirectAttributes.addFlashAttribute("success", "Client modifié avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error", "Client non trouvé");
        }
        return "redirect:/clients";
    }
    
    /**
     * Supprime un client
     */
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = clientService.deleteClient(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Client supprimé avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error", "Client non trouvé");
        }
        return "redirect:/clients";
    }
    
    /**
     * Recherche des clients par nom
     */
    @GetMapping("/search")
    public String searchClients(@RequestParam String nom, Model model) {
        List<Client> clients = clientService.searchClientsByNom(nom);
        model.addAttribute("clients", clients);
        model.addAttribute("title", "Résultats de recherche pour: " + nom);
        model.addAttribute("searchTerm", nom);
        return "client/list";
    }
}
