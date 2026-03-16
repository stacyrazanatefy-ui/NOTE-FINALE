package com.example.note.repository;

import com.example.note.entity.Parametre;
import com.example.note.entity.Matiere;
import com.example.note.entity.Operateur;
import com.example.note.entity.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Long> {
    
    List<Parametre> findByMatiere(Matiere matiere);
    
    List<Parametre> findByOperateur(Operateur operateur);
    
    List<Parametre> findByResolution(Resolution resolution);
    
    List<Parametre> findByMatiereId(Long matiereId);
    
    List<Parametre> findByOperateurId(Long operateurId);
    
    List<Parametre> findByResolutionId(Long resolutionId);
    
    @Query("SELECT p FROM Parametre p WHERE p.matiere.id = :matiereId AND p.operateur.id = :operateurId")
    List<Parametre> findByMatiereIdAndOperateurId(@Param("matiereId") Long matiereId, 
                                                  @Param("operateurId") Long operateurId);
    
    @Query("SELECT p FROM Parametre p WHERE p.matiere.id = :matiereId AND p.resolution.id = :resolutionId")
    List<Parametre> findByMatiereIdAndResolutionId(@Param("matiereId") Long matiereId, 
                                                   @Param("resolutionId") Long resolutionId);
    
    List<Parametre> findByDiff(BigDecimal diff);
}
