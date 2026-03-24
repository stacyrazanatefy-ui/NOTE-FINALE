package com.example.note.service;

import com.example.note.dto.NoteFinaleResponse;
import com.example.note.entity.*;
import com.example.note.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteFinaleService {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private ParametreRepository parametreRepository;

    public NoteFinaleResponse calculerNoteFinale(Long candidatId, Long matiereId) {
        try {
            // Étape 1: Récupérer toutes les notes du candidat dans la matière
            Optional<Candidat> candidatOpt = candidatRepository.findById(candidatId);
            Optional<Matiere> matiereOpt = matiereRepository.findById(matiereId);
            
            if (candidatOpt.isEmpty()) {
                throw new IllegalArgumentException("Candidat avec ID " + candidatId + " non trouvé");
            }
            if (matiereOpt.isEmpty()) {
                throw new IllegalArgumentException("Matière avec ID " + matiereId + " non trouvée");
            }
            
            Candidat candidat = candidatOpt.get();
            Matiere matiere = matiereOpt.get();
            
            List<Note> notes = noteRepository.findByCandidat(candidat).stream()
                    .filter(note -> note.getMatiere().getId().equals(matiereId))
                    .collect(Collectors.toList());
            
            if (notes.isEmpty()) {
                throw new IllegalArgumentException("Aucune note trouvée pour le candidat " + candidat.getNom() + " dans la matière " + matiere.getNom());
            }
            
            // Étape 1: Récupération des notes
            List<BigDecimal> valeursNotes = notes.stream()
                    .map(Note::getNote)
                    .collect(Collectors.toList());
            
            // Étape 2: Cas particuliers
            // Si 1 seule note
            if (valeursNotes.size() == 1) {
                return new NoteFinaleResponse(
                        candidatId, candidat.getNom(),
                        matiereId, matiere.getNom(),
                        valeursNotes, valeursNotes.get(0),
                        "Une seule note", "Cas particulier: une note", "Direct"
                );
            }
            
            // Si toutes les notes identiques
            if (toutesNotesIdentiques(valeursNotes)) {
                return new NoteFinaleResponse(
                        candidatId, candidat.getNom(),
                        matiereId, matiere.getNom(),
                        valeursNotes, valeursNotes.get(0),
                        "Toutes les notes identiques", "Cas particulier: notes identiques", "Direct"
                );
            }
            
            // Étape 3: Calcul de la différence totale
            double differenceTotale = calculerDifferenceTotale(valeursNotes);
            
            // Étape 4: Recherche des paramètres applicables
            List<Parametre> parametres = parametreRepository.findByMatiere(matiere);
            List<Parametre> parametresApplicables = new java.util.ArrayList<>();
            
            // Filtrer les paramètres qui satisfont la condition
            for (Parametre parametre : parametres) {
                String operateur = parametre.getOperateur().getOperateur();
                double differenceParametre = parametre.getDiff().doubleValue();
                
                boolean condition = evaluerCondition(differenceTotale, operateur, differenceParametre);
                
                if (condition) {
                    parametresApplicables.add(parametre);
                }
            }
            
            // Étape 5: Choix du meilleur paramètre (le plus proche)
            if (!parametresApplicables.isEmpty()) {
                Parametre meilleurParametre = choisirMeilleurParametre(parametresApplicables, differenceTotale);
                String resolution = meilleurParametre.getResolution().getNom();
                BigDecimal resultat = appliquerResolution(valeursNotes, resolution);
                
                return new NoteFinaleResponse(
                        candidatId, candidat.getNom(),
                        matiereId, matiere.getNom(),
                        valeursNotes, resultat,
                        "Meilleur paramètre sélectionné", 
                        String.format("Diff: %.2f, Seuil: %.2f, Distance: %.2f -> %s", 
                            differenceTotale, 
                            meilleurParametre.getDiff().doubleValue(),
                            Math.abs(differenceTotale - meilleurParametre.getDiff().doubleValue()),
                            resolution),
                        resolution
                );
            }
            
            // Étape 6: Cas par défaut - si aucune condition n'est remplie
            double moyenne = valeursNotes.stream()
                    .mapToDouble(BigDecimal::doubleValue)
                    .average()
                    .orElse(valeursNotes.get(0).doubleValue());
            
            return new NoteFinaleResponse(
                    candidatId, candidat.getNom(),
                    matiereId, matiere.getNom(),
                    valeursNotes, BigDecimal.valueOf(moyenne).setScale(2, RoundingMode.HALF_UP),
                    "Aucun paramètre match", "Cas par défaut: moyenne", "Moyenne"
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du calcul de la note finale: " + e.getMessage(), e);
        }
    }
    
    private boolean toutesNotesIdentiques(List<BigDecimal> notes) {
        if (notes.isEmpty()) return true;
        BigDecimal premiereNote = notes.get(0);
        return notes.stream().allMatch(note -> note.equals(premiereNote));
    }
    
    private double calculerDifferenceTotale(List<BigDecimal> notes) {
        double differenceTotale = 0;
        for (int i = 0; i < notes.size(); i++) {
            for (int j = i + 1; j < notes.size(); j++) {
                differenceTotale += Math.abs(notes.get(i).doubleValue() - notes.get(j).doubleValue());
            }
        }
        return differenceTotale;
    }
    
    private boolean evaluerCondition(double differenceTotale, String operateur, double differenceParametre) {
        switch (operateur.trim()) {
            case ">":
                return differenceTotale > differenceParametre;
            case ">=":
                return differenceTotale >= differenceParametre;
            case "<":
                return differenceTotale < differenceParametre;
            case "<=":
                return differenceTotale <= differenceParametre;
            case "=":
            case "==":
                return Math.abs(differenceTotale - differenceParametre) < 0.001;
            case "!=":
            case "<>":
                return Math.abs(differenceTotale - differenceParametre) >= 0.001;
            default:
                throw new IllegalArgumentException("Opérateur non reconnu: " + operateur);
        }
    }
    
    private Parametre choisirMeilleurParametre(List<Parametre> parametresApplicables, double differenceTotale) {
        Parametre meilleurParametre = null;
        double distanceMinimale = Double.MAX_VALUE;
        
        for (Parametre parametre : parametresApplicables) {
            double distance = Math.abs(differenceTotale - parametre.getDiff().doubleValue());
            
            if (distance < distanceMinimale) {
                distanceMinimale = distance;
                meilleurParametre = parametre;
            } else if (distance == distanceMinimale && meilleurParametre != null) {
                // En cas d'égalité, prendre le seuil le plus petit
                if (parametre.getDiff().doubleValue() < meilleurParametre.getDiff().doubleValue()) {
                    meilleurParametre = parametre;
                }
            }
        }
        
        return meilleurParametre;
    }
    
    private BigDecimal appliquerResolution(List<BigDecimal> notes, String resolutionNom) {
        switch (resolutionNom.toLowerCase().trim()) {
            case "petit":
            case "plus petite":
                return notes.stream().min(BigDecimal::compareTo).orElse(notes.get(0));
            case "grand":
            case "plus grande":
                return notes.stream().max(BigDecimal::compareTo).orElse(notes.get(0));
            case "moyenne":
                double moyenne = notes.stream()
                        .mapToDouble(BigDecimal::doubleValue)
                        .average()
                        .orElse(notes.get(0).doubleValue());
                return BigDecimal.valueOf(moyenne).setScale(2, RoundingMode.HALF_UP);
            default:
                return notes.get(0);
        }
    }
}
