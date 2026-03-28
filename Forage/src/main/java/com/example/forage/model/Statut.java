package com.example.forage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "statuts")
public class Statut {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 50, message = "Le libellé ne doit pas dépasser 50 caractères")
    @Column(nullable = false, unique = true)
    private String libelle;
    
    // Constructeurs
    public Statut() {}
    
    public Statut(String libelle) {
        this.libelle = libelle;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    @Override
    public String toString() {
        return "Statut{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
