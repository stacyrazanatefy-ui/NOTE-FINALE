package com.example.note.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "parametre")
public class Parametre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La matière est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmatiere", nullable = false)
    @JsonIgnoreProperties({"parametres"})
    private Matiere matiere;
    
    @Column(name = "diff")
    private BigDecimal diff;
    
    @NotNull(message = "L'opérateur est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idoperateur", nullable = false)
    @JsonIgnoreProperties({"parametres"})
    private Operateur operateur;
    
    @NotNull(message = "La résolution est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idresolution", nullable = false)
    @JsonIgnoreProperties({"parametres"})
    private Resolution resolution;
    
    public Parametre() {}
    
    public Parametre(Matiere matiere, BigDecimal diff, Operateur operateur, Resolution resolution) {
        this.matiere = matiere;
        this.diff = diff;
        this.operateur = operateur;
        this.resolution = resolution;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    public BigDecimal getDiff() {
        return diff;
    }
    
    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }
    
    public Operateur getOperateur() {
        return operateur;
    }
    
    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }
    
    public Resolution getResolution() {
        return resolution;
    }
    
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
}
