package com.example.note.controller;

import com.example.note.entity.Operateur;
import com.example.note.dto.OperateurDTO;
import com.example.note.repository.OperateurRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/operateurs")
@CrossOrigin(origins = "*")
public class OperateurController {

    @Autowired
    private OperateurRepository operateurRepository;

    @GetMapping
    public ResponseEntity<List<OperateurDTO>> getAllOperateurs() {
        List<Operateur> operateurs = operateurRepository.findAll();
        List<OperateurDTO> operateursDTO = operateurs.stream()
                .map(op -> new OperateurDTO(op.getId(), op.getOperateur()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(operateursDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operateur> getOperateurById(@PathVariable Long id) {
        Optional<Operateur> operateur = operateurRepository.findById(id);
        return operateur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/operateur/{symbole}")
    public ResponseEntity<Operateur> getOperateurBySymbole(@PathVariable String symbole) {
        Optional<Operateur> operateur = operateurRepository.findByOperateur(symbole);
        return operateur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Operateur> createOperateur(@Valid @RequestBody Operateur operateur) {
        Operateur savedOperateur = operateurRepository.save(operateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOperateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operateur> updateOperateur(@PathVariable Long id, 
                                                    @Valid @RequestBody Operateur operateurDetails) {
        return operateurRepository.findById(id)
                .map(operateur -> {
                    operateur.setOperateur(operateurDetails.getOperateur());
                    Operateur updatedOperateur = operateurRepository.save(operateur);
                    return ResponseEntity.ok(updatedOperateur);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperateur(@PathVariable Long id) {
        return operateurRepository.findById(id)
                .map(operateur -> {
                    operateurRepository.delete(operateur);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
