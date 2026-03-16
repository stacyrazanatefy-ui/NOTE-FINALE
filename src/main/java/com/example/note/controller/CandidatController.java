package com.example.note.controller;

import com.example.note.entity.Candidat;
import com.example.note.dto.CandidatDTO;
import com.example.note.repository.CandidatRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidats")
@CrossOrigin(origins = "*")
public class CandidatController {

    @Autowired
    private CandidatRepository candidatRepository;

    @GetMapping
    public ResponseEntity<List<CandidatDTO>> getAllCandidats() {
        List<Candidat> candidats = candidatRepository.findAll();
        List<CandidatDTO> candidatDTOs = candidats.stream()
                .map(c -> new CandidatDTO(c.getId(), c.getNom()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(candidatDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidat> getCandidatById(@PathVariable Long id) {
        Optional<Candidat> candidat = candidatRepository.findById(id);
        return candidat.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Candidat> getCandidatByNom(@PathVariable String nom) {
        Optional<Candidat> candidat = candidatRepository.findByNom(nom);
        return candidat.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Candidat>> searchCandidats(@PathVariable String keyword) {
        List<Candidat> candidats = candidatRepository.findByNomContainingIgnoreCase(keyword);
        return ResponseEntity.ok(candidats);
    }

    @PostMapping
    public ResponseEntity<Candidat> createCandidat(@Valid @RequestBody Candidat candidat) {
        if (candidatRepository.existsByNom(candidat.getNom())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Candidat savedCandidat = candidatRepository.save(candidat);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidat> updateCandidat(@PathVariable Long id, 
                                                   @Valid @RequestBody Candidat candidatDetails) {
        return candidatRepository.findById(id)
                .map(candidat -> {
                    candidat.setNom(candidatDetails.getNom());
                    Candidat updatedCandidat = candidatRepository.save(candidat);
                    return ResponseEntity.ok(updatedCandidat);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidat(@PathVariable Long id) {
        return candidatRepository.findById(id)
                .map(candidat -> {
                    candidatRepository.delete(candidat);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
