package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.services.ISubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Contrôleur gérant les opérations sur les thèmes.
 * Fournit un point de terminaison pour récupérer les thèmes.
 */

@RestController
@RequiredArgsConstructor
public class SubjectController {

    private  final ISubjectService subjectService;

    /**
     * Récupère tous les thèmes correspondant aux critères spécifiés.
     *
     * @param subjectDto l'objet de transfert de données contenant les critères de récupération des thèmes
     * @return une Map contenant une liste de Subject représentant tous les thèmes trouvés
     */
    @GetMapping("/subject")
    public ResponseEntity<Map<String, List<Subject>>> getSubject(SubjectDto subjectDto) {
        List<Subject> subjects = subjectService.getSubject(subjectDto);
        return ResponseEntity.ok(Map.of("subject", subjects));
    }

    /**
     * Récupère tous les thèmes associés à un utilisateur spécifique.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une ResponseEntity contenant la liste des thèmes associés à l'utilisateur
     */
    @GetMapping("/subjects/user/{userId}")
    public ResponseEntity<List<Subject>> getSubjectsForUser(@PathVariable Long userId) {
        List<Subject> subjects = subjectService.getSubjectsForUser(userId);
        return ResponseEntity.ok(subjects);
    }
}
