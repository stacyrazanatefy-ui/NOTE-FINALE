package com.example.note.repository;

import com.example.note.entity.Note;
import com.example.note.entity.Candidat;
import com.example.note.entity.Matiere;
import com.example.note.entity.Correcteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    List<Note> findByCandidat(Candidat candidat);
    
    List<Note> findByMatiere(Matiere matiere);
    
    List<Note> findByCorrecteur(Correcteur correcteur);
    
    Optional<Note> findByCandidatAndMatiere(Candidat candidat, Matiere matiere);
    
    @Query("SELECT n FROM Note n WHERE n.candidat.id = :candidatId AND n.matiere.id = :matiereId")
    Optional<Note> findByCandidatIdAndMatiereId(@Param("candidatId") Long candidatId, 
                                               @Param("matiereId") Long matiereId);
    
    @Query("SELECT AVG(n.note) FROM Note n WHERE n.matiere.id = :matiereId")
    BigDecimal findAverageNoteByMatiere(@Param("matiereId") Long matiereId);
    
    @Query("SELECT AVG(n.note) FROM Note n WHERE n.candidat.id = :candidatId")
    BigDecimal findAverageNoteByCandidat(@Param("candidatId") Long candidatId);
    
    List<Note> findByNoteBetween(BigDecimal minNote, BigDecimal maxNote);
}
