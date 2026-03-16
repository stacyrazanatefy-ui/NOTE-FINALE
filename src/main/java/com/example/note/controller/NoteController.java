package com.example.note.controller;

import com.example.note.entity.Note;
import com.example.note.entity.Candidat;
import com.example.note.entity.Matiere;
import com.example.note.entity.Correcteur;
import com.example.note.dto.NoteDTO;
import com.example.note.repository.NoteRepository;
import com.example.note.repository.CandidatRepository;
import com.example.note.repository.MatiereRepository;
import com.example.note.repository.CorrecteurRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private CorrecteurRepository correcteurRepository;

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        List<NoteDTO> noteDTOs = notes.stream()
                .map(n -> {
                    NoteDTO dto = new NoteDTO();
                    dto.setId(n.getId());
                    dto.setNote(n.getNote());
                    dto.setCandidatNom(n.getCandidat() != null ? n.getCandidat().getNom() : "N/A");
                    dto.setMatiereNom(n.getMatiere() != null ? n.getMatiere().getNom() : "N/A");
                    dto.setCorrecteurNom(n.getCorrecteur() != null ? n.getCorrecteur().getNom() : "N/A");
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(noteDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<Note>> getNotesByCandidat(@PathVariable Long candidatId) {
        Optional<Candidat> candidat = candidatRepository.findById(candidatId);
        if (candidat.isPresent()) {
            List<Note> notes = noteRepository.findByCandidat(candidat.get());
            return ResponseEntity.ok(notes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/matiere/{matiereId}")
    public ResponseEntity<List<Note>> getNotesByMatiere(@PathVariable Long matiereId) {
        Optional<Matiere> matiere = matiereRepository.findById(matiereId);
        if (matiere.isPresent()) {
            List<Note> notes = noteRepository.findByMatiere(matiere.get());
            return ResponseEntity.ok(notes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/correcteur/{correcteurId}")
    public ResponseEntity<List<Note>> getNotesByCorrecteur(@PathVariable Long correcteurId) {
        Optional<Correcteur> correcteur = correcteurRepository.findById(correcteurId);
        if (correcteur.isPresent()) {
            List<Note> notes = noteRepository.findByCorrecteur(correcteur.get());
            return ResponseEntity.ok(notes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/candidat/{candidatId}/matiere/{matiereId}")
    public ResponseEntity<Note> getNoteByCandidatAndMatiere(@PathVariable Long candidatId, 
                                                             @PathVariable Long matiereId) {
        Optional<Note> note = noteRepository.findByCandidatIdAndMatiereId(candidatId, matiereId);
        return note.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/range/{min}/{max}")
    public ResponseEntity<List<Note>> getNotesByRange(@PathVariable BigDecimal min, 
                                                      @PathVariable BigDecimal max) {
        List<Note> notes = noteRepository.findByNoteBetween(min, max);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
        Optional<Candidat> candidat = candidatRepository.findById(note.getCandidat().getId());
        Optional<Matiere> matiere = matiereRepository.findById(note.getMatiere().getId());
        Optional<Correcteur> correcteur = correcteurRepository.findById(note.getCorrecteur().getId());
        
        if (candidat.isPresent() && matiere.isPresent() && correcteur.isPresent()) {
            note.setCandidat(candidat.get());
            note.setMatiere(matiere.get());
            note.setCorrecteur(correcteur.get());
            Note savedNote = noteRepository.save(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, 
                                          @Valid @RequestBody Note noteDetails) {
        return noteRepository.findById(id)
                .map(note -> {
                    note.setNote(noteDetails.getNote());
                    
                    Optional<Candidat> candidat = candidatRepository.findById(noteDetails.getCandidat().getId());
                    Optional<Matiere> matiere = matiereRepository.findById(noteDetails.getMatiere().getId());
                    Optional<Correcteur> correcteur = correcteurRepository.findById(noteDetails.getCorrecteur().getId());
                    
                    if (candidat.isPresent() && matiere.isPresent() && correcteur.isPresent()) {
                        note.setCandidat(candidat.get());
                        note.setMatiere(matiere.get());
                        note.setCorrecteur(correcteur.get());
                        Note updatedNote = noteRepository.save(note);
                        return ResponseEntity.ok(updatedNote);
                    }
                    return ResponseEntity.badRequest().<Note>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
