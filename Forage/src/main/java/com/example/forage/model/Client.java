package com.example.forage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(nullable = false)
    private String nom;
    
    @NotBlank(message = "Le contact est obligatoire")
    @Size(max = 20, message = "Le contact ne doit pas dépasser 20 caractères")
    @Column(nullable = false, unique = true)
    private String contact;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demande> demandes;
    
    // Constructeurs
    public Client() {}
    
    public Client(String nom, String contact) {
        this.nom = nom;
        this.contact = contact;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public List<Demande> getDemandes() {
        return demandes;
    }
    
    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }
    
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
