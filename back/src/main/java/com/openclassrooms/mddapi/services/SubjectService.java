package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * Service fournissant des opérations de gestion des thèmes.
 * Gère la récupération, la mise à jour des thèmes.
 */

@Service
@RequiredArgsConstructor
public class SubjectService implements ISubjectService {

    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    private final SubjectMapper subjectMapper;


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

    public List<Subject> getSubjectsForUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new ArrayList<>(user.getSubjects());
    }

}
