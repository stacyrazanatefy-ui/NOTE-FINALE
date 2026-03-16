package com.example.note.controller;

import com.example.note.entity.Parametre;
import com.example.note.entity.Matiere;
import com.example.note.entity.Operateur;
import com.example.note.entity.Resolution;
import com.example.note.dto.ParametreDTO;
import com.example.note.repository.ParametreRepository;
import com.example.note.repository.MatiereRepository;
import com.example.note.repository.OperateurRepository;
import com.example.note.repository.ResolutionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/parametres")
@CrossOrigin(origins = "*")
public class ParametreController {

    @Autowired
    private ParametreRepository parametreRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private OperateurRepository operateurRepository;
    
    @Autowired
    private ResolutionRepository resolutionRepository;

    @GetMapping
    public ResponseEntity<List<ParametreDTO>> getAllParametres() {
        List<Parametre> parametres = parametreRepository.findAll();
        List<ParametreDTO> parametresDTO = parametres.stream()
                .map(param -> new ParametreDTO(
                    param.getId(),
                    param.getMatiere().getNom(),
                    param.getDiff(),
                    param.getOperateur().getOperateur(),
                    param.getResolution().getNom()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(parametresDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parametre> getParametreById(@PathVariable Long id) {
        Optional<Parametre> parametre = parametreRepository.findById(id);
        return parametre.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/edit")
    public ResponseEntity<ParametreDTO> getParametreForEdit(@PathVariable Long id) {
        Optional<Parametre> parametre = parametreRepository.findById(id);
        if (parametre.isPresent()) {
            Parametre p = parametre.get();
            ParametreDTO dto = new ParametreDTO(
                p.getId(),
                p.getMatiere().getNom(),
                p.getDiff(),
                p.getOperateur().getOperateur(),
                p.getResolution().getNom()
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/matiere/{matiereId}")
    public ResponseEntity<List<Parametre>> getParametresByMatiere(@PathVariable Long matiereId) {
        Optional<Matiere> matiere = matiereRepository.findById(matiereId);
        if (matiere.isPresent()) {
            List<Parametre> parametres = parametreRepository.findByMatiere(matiere.get());
            return ResponseEntity.ok(parametres);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/operateur/{operateurId}")
    public ResponseEntity<List<Parametre>> getParametresByOperateur(@PathVariable Long operateurId) {
        Optional<Operateur> operateur = operateurRepository.findById(operateurId);
        if (operateur.isPresent()) {
            List<Parametre> parametres = parametreRepository.findByOperateur(operateur.get());
            return ResponseEntity.ok(parametres);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/resolution/{resolutionId}")
    public ResponseEntity<List<Parametre>> getParametresByResolution(@PathVariable Long resolutionId) {
        Optional<Resolution> resolution = resolutionRepository.findById(resolutionId);
        if (resolution.isPresent()) {
            List<Parametre> parametres = parametreRepository.findByResolution(resolution.get());
            return ResponseEntity.ok(parametres);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/matiere/{matiereId}/operateur/{operateurId}")
    public ResponseEntity<List<Parametre>> getParametresByMatiereAndOperateur(@PathVariable Long matiereId,
                                                                              @PathVariable Long operateurId) {
        List<Parametre> parametres = parametreRepository.findByMatiereIdAndOperateurId(matiereId, operateurId);
        return ResponseEntity.ok(parametres);
    }

    @GetMapping("/matiere/{matiereId}/resolution/{resolutionId}")
    public ResponseEntity<List<Parametre>> getParametresByMatiereAndResolution(@PathVariable Long matiereId,
                                                                               @PathVariable Long resolutionId) {
        List<Parametre> parametres = parametreRepository.findByMatiereIdAndResolutionId(matiereId, resolutionId);
        return ResponseEntity.ok(parametres);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Parametre>> searchParametres(@PathVariable String keyword) {
        try {
            BigDecimal searchValue = new BigDecimal(keyword);
            List<Parametre> parametres = parametreRepository.findByDiff(searchValue);
            return ResponseEntity.ok(parametres);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Parametre> createParametre(@Valid @RequestBody Parametre parametre) {
        Optional<Matiere> matiere = matiereRepository.findById(parametre.getMatiere().getId());
        Optional<Operateur> operateur = operateurRepository.findById(parametre.getOperateur().getId());
        Optional<Resolution> resolution = resolutionRepository.findById(parametre.getResolution().getId());
        
        if (matiere.isPresent() && operateur.isPresent() && resolution.isPresent()) {
            parametre.setMatiere(matiere.get());
            parametre.setOperateur(operateur.get());
            parametre.setResolution(resolution.get());
            Parametre savedParametre = parametreRepository.save(parametre);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedParametre);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parametre> updateParametre(@PathVariable Long id, 
                                                    @Valid @RequestBody Parametre parametreDetails) {
        return parametreRepository.findById(id)
                .map(parametre -> {
                    parametre.setDiff(parametreDetails.getDiff());
                    
                    Optional<Matiere> matiere = matiereRepository.findById(parametreDetails.getMatiere().getId());
                    Optional<Operateur> operateur = operateurRepository.findById(parametreDetails.getOperateur().getId());
                    Optional<Resolution> resolution = resolutionRepository.findById(parametreDetails.getResolution().getId());
                    
                    if (matiere.isPresent() && operateur.isPresent() && resolution.isPresent()) {
                        parametre.setMatiere(matiere.get());
                        parametre.setOperateur(operateur.get());
                        parametre.setResolution(resolution.get());
                        Parametre updatedParametre = parametreRepository.save(parametre);
                        return ResponseEntity.ok(updatedParametre);
                    }
                    return ResponseEntity.badRequest().<Parametre>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParametre(@PathVariable Long id) {
        return parametreRepository.findById(id)
                .map(parametre -> {
                    parametreRepository.delete(parametre);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
