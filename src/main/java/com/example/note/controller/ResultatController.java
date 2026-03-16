package com.example.note.controller;

import com.example.note.dto.NoteFinaleResponse;
import com.example.note.service.NoteFinaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ResultatController {

    @Autowired
    private NoteFinaleService noteFinaleService;

    @GetMapping("/resultat")
    public ResponseEntity<?> getResultat(@RequestParam Long idCandidat, @RequestParam Long idMatiere) {
        try {
            NoteFinaleResponse result = noteFinaleService.calculerNoteFinale(idCandidat, idMatiere);
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
