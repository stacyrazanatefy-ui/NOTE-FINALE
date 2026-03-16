package com.example.note.controller;

import com.example.note.entity.Matiere;
import com.example.note.dto.MatiereDTO;
import com.example.note.repository.MatiereRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matieres")
@CrossOrigin(origins = "*")
public class MatiereController {

    @Autowired
    private MatiereRepository matiereRepository;

    @GetMapping
    public ResponseEntity<List<MatiereDTO>> getAllMatieres() {
        List<Matiere> matieres = matiereRepository.findAll();
        List<MatiereDTO> matiereDTOs = matieres.stream()
                .map(m -> new MatiereDTO(m.getId(), m.getNom()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(matiereDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matiere> getMatiereById(@PathVariable Long id) {
        Optional<Matiere> matiere = matiereRepository.findById(id);
        return matiere.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Matiere> getMatiereByNom(@PathVariable String nom) {
        Optional<Matiere> matiere = matiereRepository.findByNom(nom);
        return matiere.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Matiere>> searchMatieres(@PathVariable String keyword) {
        List<Matiere> matieres = matiereRepository.findByNomContainingIgnoreCase(keyword);
        return ResponseEntity.ok(matieres);
    }

    @PostMapping
    public ResponseEntity<Matiere> createMatiere(@Valid @RequestBody Matiere matiere) {
        if (matiereRepository.existsByNom(matiere.getNom())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Matiere savedMatiere = matiereRepository.save(matiere);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMatiere);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matiere> updateMatiere(@PathVariable Long id, 
                                               @Valid @RequestBody Matiere matiereDetails) {
        return matiereRepository.findById(id)
                .map(matiere -> {
                    matiere.setNom(matiereDetails.getNom());
                    Matiere updatedMatiere = matiereRepository.save(matiere);
                    return ResponseEntity.ok(updatedMatiere);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        return matiereRepository.findById(id)
                .map(matiere -> {
                    matiereRepository.delete(matiere);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
