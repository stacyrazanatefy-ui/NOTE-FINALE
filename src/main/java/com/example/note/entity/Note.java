package com.example.note.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "note")
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La note est obligatoire")
    @DecimalMin(value = "0.0", message = "La note doit être supérieure ou égale à 0")
    @Column(name = "note", nullable = false)
    private BigDecimal note;
    
    @NotNull(message = "Le candidat est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcandidat", nullable = false)
    private Candidat candidat;
    
    @NotNull(message = "La matière est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmatiere", nullable = false)
    private Matiere matiere;
    
    @NotNull(message = "Le correcteur est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcorrecteur", nullable = false)
    private Correcteur correcteur;
    
    public Note() {}
    
    public Note(BigDecimal note, Candidat candidat, Matiere matiere, Correcteur correcteur) {
        this.note = note;
        this.candidat = candidat;
        this.matiere = matiere;
        this.correcteur = correcteur;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getNote() {
        return note;
    }
    
    public void setNote(BigDecimal note) {
        this.note = note;
    }
    
    public Candidat getCandidat() {
        return candidat;
    }
    
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
    
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    public Correcteur getCorrecteur() {
        return correcteur;
    }
    
    public void setCorrecteur(Correcteur correcteur) {
        this.correcteur = correcteur;
    }
}
