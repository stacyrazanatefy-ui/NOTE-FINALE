package com.example.note.dto;

import java.math.BigDecimal;

public class NoteDTO {
    private Long id;
    private BigDecimal note;
    private String candidatNom;
    private String matiereNom;
    private String correcteurNom;
    private Long idcandidat;
    private Long idmatiere;
    private Long idcorrecteur;

    public NoteDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getNote() { return note; }
    public void setNote(BigDecimal note) { this.note = note; }

    public String getCandidatNom() { return candidatNom; }
    public void setCandidatNom(String candidatNom) { this.candidatNom = candidatNom; }

    public String getMatiereNom() { return matiereNom; }
    public void setMatiereNom(String matiereNom) { this.matiereNom = matiereNom; }

    public String getCorrecteurNom() { return correcteurNom; }
    public void setCorrecteurNom(String correcteurNom) { this.correcteurNom = correcteurNom; }

    public Long getIdcandidat() { return idcandidat; }
    public void setIdcandidat(Long idcandidat) { this.idcandidat = idcandidat; }

    public Long getIdmatiere() { return idmatiere; }
    public void setIdmatiere(Long idmatiere) { this.idmatiere = idmatiere; }

    public Long getIdcorrecteur() { return idcorrecteur; }
    public void setIdcorrecteur(Long idcorrecteur) { this.idcorrecteur = idcorrecteur; }
}
