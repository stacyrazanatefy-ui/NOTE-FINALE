package com.example.forage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "devis_details")
public class DevisDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Le devis est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devis_id", nullable = false)
    private Devis devis;
    
    @NotBlank(message = "Le libellé est obligatoire")
    @Column(name = "libelle", nullable = false)
    private String libelle;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire doit être supérieur à 0")
    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;
    
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    @Column(name = "quantite", nullable = false)
    private Integer quantite;
    
    @Column(name = "montant", nullable = false)
    private Double montant;
    
    @PrePersist
    @PreUpdate
    protected void calculateMontant() {
        if (prixUnitaire != null && quantite != null) {
            montant = prixUnitaire * quantite;
        }
    }
    
    // Constructeurs
    public DevisDetails() {}
    
    public DevisDetails(String libelle, Double prixUnitaire, Integer quantite) {
        this.libelle = libelle;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        calculateMontant();
    }
    
    public DevisDetails(Devis devis, String libelle, Double prixUnitaire, Integer quantite) {
        this.devis = devis;
        this.libelle = libelle;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        calculateMontant();
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Devis getDevis() {
        return devis;
    }
    
    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public Double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        calculateMontant();
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
        calculateMontant();
    }
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    // Méthodes utilitaires
    public void updateMontant() {
        calculateMontant();
    }
    
    @Override
    public String toString() {
        return "DevisDetails{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", quantite=" + quantite +
                ", montant=" + montant +
                '}';
    }
}
