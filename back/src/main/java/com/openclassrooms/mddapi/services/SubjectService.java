package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * Service fournissant des opérations de gestion des thèmes.
 * Gère la récupération, la mise à jour des thèmes.
 */

@Data
@Service
public class SubjectService {
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectMapper subjectMapper;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param subjectRepository le dépôt pour les opérations de données des thèmes
     * @param subjectMapper le mapper pour convertir entre les DTO et les modèles d'entités
     */
    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    /**
     * Récupère tous les thèmes de la base de données.
     *
     * @param subjectDto l'objet de transfert de données contenant les critères de récupération des thèmes
     * @return une liste d'objets Subject
     */
    public List<Subject> getSubject(SubjectDto subjectDto) {
        Iterable<Subject> subjects = subjectRepository.findAll();

        List<Subject> result = StreamSupport.stream(subjects.spliterator(), false)
                .collect(Collectors.toList());

        return result;
    }

}
