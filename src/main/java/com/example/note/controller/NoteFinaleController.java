package com.example.note.controller;

import com.example.note.dto.NoteFinaleResponse;
import com.example.note.service.NoteFinaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/calcul-notes")
@CrossOrigin(origins = "*")
public class NoteFinaleController {

    @Autowired
    private NoteFinaleService noteFinaleService;

    @GetMapping("/candidat/{candidatId}/matiere/{matiereId}")
    public ResponseEntity<?> calculerNoteFinale(@PathVariable Long candidatId, 
                                               @PathVariable Long matiereId) {
        try {
            NoteFinaleResponse result = noteFinaleService.calculerNoteFinale(candidatId, matiereId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur de calcul",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Erreur interne",
                "message", "Une erreur inattendue est survenue lors du calcul"
            ));
        }
    }

    @PostMapping("/calculer")
    public ResponseEntity<?> calculerNoteFinalePost(@RequestBody Map<String, Long> request) {
        Long candidatId = request.get("candidatId");
        Long matiereId = request.get("matiereId");
        
        if (candidatId == null || matiereId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Paramètres manquants",
                "message", "candidatId et matiereId sont requis"
            ));
        }
        
        try {
            NoteFinaleResponse result = noteFinaleService.calculerNoteFinale(candidatId, matiereId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur de calcul",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Erreur interne",
                "message", "Une erreur inattendue est survenue lors du calcul"
            ));
        }
    }
}
