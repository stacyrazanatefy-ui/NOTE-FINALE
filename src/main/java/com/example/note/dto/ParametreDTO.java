package com.example.note.dto;

import java.math.BigDecimal;

public class ParametreDTO {
    private Long id;
    private String matiereNom;
    private BigDecimal diff;
    private String operateurSymbole;
    private String resolutionNom;

    public ParametreDTO() {}

    public ParametreDTO(Long id, String matiereNom, BigDecimal diff, String operateurSymbole, String resolutionNom) {
        this.id = id;
        this.matiereNom = matiereNom;
        this.diff = diff;
        this.operateurSymbole = operateurSymbole;
        this.resolutionNom = resolutionNom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatiereNom() {
        return matiereNom;
    }

    public void setMatiereNom(String matiereNom) {
        this.matiereNom = matiereNom;
    }

    public BigDecimal getDiff() {
        return diff;
    }

    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }

    public String getOperateurSymbole() {
        return operateurSymbole;
    }

    public void setOperateurSymbole(String operateurSymbole) {
        this.operateurSymbole = operateurSymbole;
    }

    public String getResolutionNom() {
        return resolutionNom;
    }

    public void setResolutionNom(String resolutionNom) {
        this.resolutionNom = resolutionNom;
    }
}
