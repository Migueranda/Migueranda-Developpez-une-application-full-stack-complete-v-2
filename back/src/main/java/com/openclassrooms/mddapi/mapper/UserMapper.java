package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.model.dtos.SignUpDto;
import com.openclassrooms.mddapi.model.dtos.SubjectDto;
import com.openclassrooms.mddapi.model.dtos.UserDto;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import com.openclassrooms.mddapi.model.entities.Subject;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper implements EntityMapper<UserDto, UserEntity> {

    @Override
    public UserEntity toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return UserEntity.builder()
                .id(dto.getId())
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    @Override
    public UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserDto.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

    public UserDto toDto(UserEntity entity, List<Subject> subjects) {
        if (entity == null) {
            return null;
        }

        List<SubjectDto> subjectDtos = subjects.stream()
                .map(this::toSubjectDto)
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .subscription(subjectDtos)
                .build();
    }

    @Override
    public List<UserEntity> toEntity(List<UserDto> dtoList) {
        if (dtoList == null) {
            return null;
        }

        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> toDto(List<UserEntity> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SubjectDto toSubjectDto(Subject subject) {
        if (subject == null) {
            return null;
        }

        return SubjectDto.builder()
                .id(subject.getId())
                .title(subject.getTitle())
                .description(subject.getDescription())
                .build();
    }
    @Mapping(target = "password", ignore = true)
    public UserEntity signUpToUser(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(signUpDto.userName());
        userEntity.setEmail(signUpDto.email());

        return userEntity;
    }
}
