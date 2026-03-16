package com.example.note.controller;

import com.example.note.entity.Resolution;
import com.example.note.dto.ResolutionDTO;
import com.example.note.repository.ResolutionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resolutions")
@CrossOrigin(origins = "*")
public class ResolutionController {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @GetMapping
    public ResponseEntity<List<ResolutionDTO>> getAllResolutions() {
        List<Resolution> resolutions = resolutionRepository.findAll();
        List<ResolutionDTO> resolutionsDTO = resolutions.stream()
                .map(res -> new ResolutionDTO(res.getId(), res.getNom()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resolutionsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resolution> getResolutionById(@PathVariable Long id) {
        Optional<Resolution> resolution = resolutionRepository.findById(id);
        return resolution.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Resolution> getResolutionByNom(@PathVariable String nom) {
        Optional<Resolution> resolution = resolutionRepository.findByNom(nom);
        return resolution.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Resolution> createResolution(@Valid @RequestBody Resolution resolution) {
        Resolution savedResolution = resolutionRepository.save(resolution);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResolution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resolution> updateResolution(@PathVariable Long id, 
                                                      @Valid @RequestBody Resolution resolutionDetails) {
        return resolutionRepository.findById(id)
                .map(resolution -> {
                    resolution.setNom(resolutionDetails.getNom());
                    Resolution updatedResolution = resolutionRepository.save(resolution);
                    return ResponseEntity.ok(updatedResolution);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResolution(@PathVariable Long id) {
        return resolutionRepository.findById(id)
                .map(resolution -> {
                    resolutionRepository.delete(resolution);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
