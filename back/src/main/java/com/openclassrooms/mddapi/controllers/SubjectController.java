package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Contrôleur gérant les opérations sur les thèmes.
 * Fournit un point de terminaison pour récupérer les thèmes.
 */

@RestController
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * Récupère tous les thèmes correspondant aux critères spécifiés.
     *
     * @param subjectDto l'objet de transfert de données contenant les critères de récupération des thèmes
     * @return une Map contenant une liste de Subject représentant tous les thèmes trouvés
     */
    @GetMapping("/subject")
    public Map<String, List<Subject>> getSubject(SubjectDto subjectDto) {
        return Map.of("subject", subjectService.getSubject(subjectDto));
    }
}
