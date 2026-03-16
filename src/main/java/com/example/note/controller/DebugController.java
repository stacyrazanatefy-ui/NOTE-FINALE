package com.example.note.controller;

import com.example.note.entity.Candidat;
import com.example.note.entity.Matiere;
import com.example.note.entity.Note;
import com.example.note.entity.Parametre;
import com.example.note.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = "*")
public class DebugController {

    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private ParametreRepository parametreRepository;

    @GetMapping("/test-candidat/{id}")
    public ResponseEntity<?> testCandidat(@PathVariable Long id) {
        try {
            Candidat candidat = candidatRepository.findById(id).orElse(null);
            return ResponseEntity.ok(Map.of(
                "found", candidat != null,
                "data", candidat
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/test-matiere/{id}")
    public ResponseEntity<?> testMatiere(@PathVariable Long id) {
        try {
            Matiere matiere = matiereRepository.findById(id).orElse(null);
            return ResponseEntity.ok(Map.of(
                "found", matiere != null,
                "data", matiere
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/test-notes/{candidatId}/{matiereId}")
    public ResponseEntity<?> testNotes(@PathVariable Long candidatId, @PathVariable Long matiereId) {
        try {
            Candidat candidat = candidatRepository.findById(candidatId).orElse(null);
            if (candidat == null) {
                return ResponseEntity.ok(Map.of("error", "Candidat non trouvé"));
            }
            
            List<Note> notes = noteRepository.findByCandidat(candidat);
            List<Note> filteredNotes = notes.stream()
                    .filter(note -> note.getMatiere().getId().equals(matiereId))
                    .toList();
            
            return ResponseEntity.ok(Map.of(
                "candidat", candidat.getNom(),
                "totalNotes", notes.size(),
                "filteredNotes", filteredNotes.size(),
                "notes", filteredNotes
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", true,
                "message", e.getMessage(),
                "stack", e.getStackTrace()
            ));
        }
    }

    @GetMapping("/test-parametres/{matiereId}")
    public ResponseEntity<?> testParametres(@PathVariable Long matiereId) {
        try {
            Matiere matiere = matiereRepository.findById(matiereId).orElse(null);
            if (matiere == null) {
                return ResponseEntity.ok(Map.of("error", "Matière non trouvée"));
            }
            
            List<Parametre> parametres = parametreRepository.findByMatiere(matiere);
            return ResponseEntity.ok(Map.of(
                "matiere", matiere.getNom(),
                "parametresCount", parametres.size(),
                "parametres", parametres
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", true,
                "message", e.getMessage(),
                "stack", e.getStackTrace()
            ));
        }
    }
}
