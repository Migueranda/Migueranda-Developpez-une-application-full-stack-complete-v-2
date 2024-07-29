package com.openclassrooms.mddapi.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.model.entities.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class UserDto {

        private Long id;
        private String userName;
        private String email;
        private String token;
        private String password;
        private List<SubjectDto> subscription;

        public static UserDto convertToDto (UserEntity userEntity, List<Subject> subjects){
            UserDto userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setUserName(userEntity.getUserName());
            userDto.setEmail(userEntity.getEmail());
            userDto.setPassword(userEntity.getPassword());
            userDto.setToken(userEntity.getToken());

            userDto.setSubscription(subjects.stream()
                    .map(SubjectDto::convertToDto)
                    .collect(Collectors.toList()));
            return userDto;
        }
    }

