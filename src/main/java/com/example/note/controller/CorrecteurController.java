package com.example.note.controller;

import com.example.note.entity.Correcteur;
import com.example.note.dto.CorrecteurDTO;
import com.example.note.repository.CorrecteurRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/correcteurs")
@CrossOrigin(origins = "*")
public class CorrecteurController {

    @Autowired
    private CorrecteurRepository correcteurRepository;

    @GetMapping
    public ResponseEntity<List<CorrecteurDTO>> getAllCorrecteurs() {
        List<Correcteur> correcteurs = correcteurRepository.findAll();
        List<CorrecteurDTO> correcteurDTOs = correcteurs.stream()
                .map(c -> new CorrecteurDTO(c.getId(), c.getNom()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(correcteurDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Correcteur> getCorrecteurById(@PathVariable Long id) {
        Optional<Correcteur> correcteur = correcteurRepository.findById(id);
        return correcteur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Correcteur> getCorrecteurByNom(@PathVariable String nom) {
        Optional<Correcteur> correcteur = correcteurRepository.findByNom(nom);
        return correcteur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Correcteur>> searchCorrecteurs(@PathVariable String keyword) {
        List<Correcteur> correcteurs = correcteurRepository.findByNomContainingIgnoreCase(keyword);
        return ResponseEntity.ok(correcteurs);
    }

    @PostMapping
    public ResponseEntity<Correcteur> createCorrecteur(@Valid @RequestBody Correcteur correcteur) {
        if (correcteurRepository.existsByNom(correcteur.getNom())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Correcteur savedCorrecteur = correcteurRepository.save(correcteur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCorrecteur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Correcteur> updateCorrecteur(@PathVariable Long id, 
                                                     @Valid @RequestBody Correcteur correcteurDetails) {
        return correcteurRepository.findById(id)
                .map(correcteur -> {
                    correcteur.setNom(correcteurDetails.getNom());
                    Correcteur updatedCorrecteur = correcteurRepository.save(correcteur);
                    return ResponseEntity.ok(updatedCorrecteur);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorrecteur(@PathVariable Long id) {
        return correcteurRepository.findById(id)
                .map(correcteur -> {
                    correcteurRepository.delete(correcteur);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
