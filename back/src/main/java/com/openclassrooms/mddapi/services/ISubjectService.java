package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.repositories.SubjectRepository;

import java.util.List;

public interface ISubjectService {
    List<Subject> getSubject(SubjectDto subjectDto);

    List<Subject> getSubjectsForUser(Long userId);
}
