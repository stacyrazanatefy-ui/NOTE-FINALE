package com.example.note.dto;

public class OperateurDTO {
    private Long id;
    private String operateur;

    public OperateurDTO() {}

    public OperateurDTO(Long id, String operateur) {
        this.id = id;
        this.operateur = operateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
}
