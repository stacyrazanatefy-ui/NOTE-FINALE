package com.example.forage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_statuts")
public class DemandeStatut {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La demande est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;
    
    @NotNull(message = "Le statut est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statut_id", nullable = false)
    private Statut statut;
    
    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDateTime date;
    
    @Column(length = 255)
    private String observation;
    
    // Constructeurs
    public DemandeStatut() {}
    
    public DemandeStatut(Demande demande, Statut statut, LocalDateTime date) {
        this.demande = demande;
        this.statut = statut;
        this.date = date;
    }
    
    public DemandeStatut(Demande demande, Statut statut, LocalDateTime date, String observation) {
        this.demande = demande;
        this.statut = statut;
        this.date = date;
        this.observation = observation;
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
    
    public Statut getStatut() {
        return statut;
    }
    
    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getObservation() {
        return observation;
    }
    
    public void setObservation(String observation) {
        this.observation = observation;
    }
    
    @Override
    public String toString() {
        return "DemandeStatut{" +
                "id=" + id +
                ", demande=" + (demande != null ? demande.getId() : "null") +
                ", statut=" + (statut != null ? statut.getLibelle() : "null") +
                ", date=" + date +
                ", observation='" + observation + '\'' +
                '}';
    }
}
