package com.example.forage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "devis")
public class Devis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La demande est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;
    
    @NotBlank(message = "Le type de devis est obligatoire")
    @Column(name = "type_devis", nullable = false)
    private String typeDevis;
    
    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutDevis statut;
    
    @Column(name = "statut_personnalise_libelle")
    private String statutPersonnaliseLibelle;
    
    @Column(name = "montant_total", nullable = false)
    private Double montantTotal;
    
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DevisDetails> details;
    
    public enum StatutDevis {
        CREE("Créé"),
        ACCEPTE("Accepté"),
        REFUSE("Refusé");
        
        private final String libelle;
        
        StatutDevis(String libelle) {
            this.libelle = libelle;
        }
        
        public String getLibelle() {
            return libelle;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        if (statut == null) {
            statut = StatutDevis.CREE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
    
    // Constructeurs
    public Devis() {}
    
    public Devis(Demande demande, String typeDevis, Double montantTotal) {
        this.demande = demande;
        this.typeDevis = typeDevis;
        this.montantTotal = montantTotal;
        this.statut = StatutDevis.CREE;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Demande getDemande() {
        return demande;
    }
    
    public void setDemande(Demande demande) {
        this.demande = demande;
    }
    
    public String getTypeDevis() {
        return typeDevis;
    }
    
    public void setTypeDevis(String typeDevis) {
        this.typeDevis = typeDevis;
    }
    
    public StatutDevis getStatut() {
        return statut;
    }
    
    public void setStatut(StatutDevis statut) {
        this.statut = statut;
    }
    
    public String getStatutPersonnaliseLibelle() {
        return statutPersonnaliseLibelle;
    }
    
    public void setStatutPersonnaliseLibelle(String statutPersonnaliseLibelle) {
        this.statutPersonnaliseLibelle = statutPersonnaliseLibelle;
    }
    
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public LocalDateTime getDateModification() {
        return dateModification;
    }
    
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
    
    public List<DevisDetails> getDetails() {
        return details;
    }
    
    public void setDetails(List<DevisDetails> details) {
        this.details = details;
    }
    
    // Méthodes utilitaires
    public String getStatutLibelle() {
        // Si un libellé personnalisé est défini, l'utiliser
        if (statutPersonnaliseLibelle != null && !statutPersonnaliseLibelle.trim().isEmpty()) {
            return statutPersonnaliseLibelle;
        }
        // Sinon, utiliser le libellé de l'énumération
        return statut != null ? statut.getLibelle() : "";
    }
    
    @Override
    public String toString() {
        return "Devis{" +
                "id=" + id +
                ", typeDevis='" + typeDevis + '\'' +
                ", statut=" + statut +
                ", montantTotal=" + montantTotal +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
