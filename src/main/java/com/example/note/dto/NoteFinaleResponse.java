package com.example.note.dto;

import java.math.BigDecimal;
import java.util.List;

public class NoteFinaleResponse {
    
    private Long candidatId;
    private String candidatNom;
    private Long matiereId;
    private String matiereNom;
    private List<BigDecimal> notesOriginales;
    private BigDecimal noteFinale;
    private String methodeCalcul;
    private String conditionAppliquee;
    private String resolutionAppliquee;
    
    public NoteFinaleResponse() {}
    
    public NoteFinaleResponse(Long candidatId, String candidatNom, Long matiereId, String matiereNom,
                              List<BigDecimal> notesOriginales, BigDecimal noteFinale,
                              String methodeCalcul, String conditionAppliquee, String resolutionAppliquee) {
        this.candidatId = candidatId;
        this.candidatNom = candidatNom;
        this.matiereId = matiereId;
        this.matiereNom = matiereNom;
        this.notesOriginales = notesOriginales;
        this.noteFinale = noteFinale;
        this.methodeCalcul = methodeCalcul;
        this.conditionAppliquee = conditionAppliquee;
        this.resolutionAppliquee = resolutionAppliquee;
    }
    
    public Long getCandidatId() {
        return candidatId;
    }
    
    public void setCandidatId(Long candidatId) {
        this.candidatId = candidatId;
    }
    
    public String getCandidatNom() {
        return candidatNom;
    }
    
    public void setCandidatNom(String candidatNom) {
        this.candidatNom = candidatNom;
    }
    
    public Long getMatiereId() {
        return matiereId;
    }
    
    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
    }
    
    public String getMatiereNom() {
        return matiereNom;
    }
    
    public void setMatiereNom(String matiereNom) {
        this.matiereNom = matiereNom;
    }
    
    public List<BigDecimal> getNotesOriginales() {
        return notesOriginales;
    }
    
    public void setNotesOriginales(List<BigDecimal> notesOriginales) {
        this.notesOriginales = notesOriginales;
    }
    
    public BigDecimal getNoteFinale() {
        return noteFinale;
    }
    
    public void setNoteFinale(BigDecimal noteFinale) {
        this.noteFinale = noteFinale;
    }
    
    public String getMethodeCalcul() {
        return methodeCalcul;
    }
    
    public void setMethodeCalcul(String methodeCalcul) {
        this.methodeCalcul = methodeCalcul;
    }
    
    public String getConditionAppliquee() {
        return conditionAppliquee;
    }
    
    public void setConditionAppliquee(String conditionAppliquee) {
        this.conditionAppliquee = conditionAppliquee;
    }
    
    public String getResolutionAppliquee() {
        return resolutionAppliquee;
    }
    
    public void setResolutionAppliquee(String resolutionAppliquee) {
        this.resolutionAppliquee = resolutionAppliquee;
    }
}
