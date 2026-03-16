package com.example.note.controller;

import com.example.note.entity.Candidat;
import com.example.note.repository.CandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestSimpleController {

    @Autowired
    private CandidatRepository candidatRepository;

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(Map.of(
            "message", "Hello World!",
            "status", "API fonctionne"
        ));
    }

    @GetMapping("/candidats")
    public ResponseEntity<?> getAllCandidats() {
        try {
            List<Candidat> candidats = candidatRepository.findAll();
            return ResponseEntity.ok(Map.of(
                "count", candidats.size(),
                "data", candidats
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/candidat/{id}")
    public ResponseEntity<?> getCandidat(@PathVariable Long id) {
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
}
