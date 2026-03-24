package com.example.forage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "demandes")
public class Demande {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;
    
    @NotBlank(message = "Le lieu est obligatoire")
    @Size(max = 200, message = "Le lieu ne doit pas dépasser 200 caractères")
    @Column(nullable = false)
    private String lieu;
    
    @NotBlank(message = "Le district est obligatoire")
    @Size(max = 100, message = "Le district ne doit pas dépasser 100 caractères")
    @Column(nullable = false)
    private String district;
    
    @NotNull(message = "Le client est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    // Constructeurs
    public Demande() {}
    
    public Demande(LocalDate date, String lieu, String district, Client client) {
        this.date = date;
        this.lieu = lieu;
        this.district = district;
        this.client = client;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getLieu() {
        return lieu;
    }
    
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", date=" + date +
                ", lieu='" + lieu + '\'' +
                ", district='" + district + '\'' +
                ", client=" + (client != null ? client.getNom() : "null") +
                '}';
    }
}
