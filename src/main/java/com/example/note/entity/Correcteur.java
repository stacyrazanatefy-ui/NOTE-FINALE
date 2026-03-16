package com.example.note.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "correcteur")
public class Correcteur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom du correcteur est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @OneToMany(mappedBy = "correcteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Note> notes;
    
    public Correcteur() {}
    
    public Correcteur(String nom) {
        this.nom = nom;
    }
    
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
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
