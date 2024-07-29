package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectMapper implements EntityMapper<SubjectDto, Subject> {

    @Override
    public Subject toEntity(SubjectDto dto) {
        if (dto == null) {
            return null;
        }

        return Subject.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    @Override
    public SubjectDto toDto(Subject entity) {
        if (entity == null) {
            return null;
        }

        return SubjectDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public List<Subject> toEntity(List<SubjectDto> dtoList) {
        if (dtoList == null) {
            return null;
        }

        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> toDto(List<Subject> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
