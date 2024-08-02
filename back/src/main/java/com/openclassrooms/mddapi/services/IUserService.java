package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dtos.CredentialsDto;
import com.openclassrooms.mddapi.model.dtos.SignUpDto;
import com.openclassrooms.mddapi.model.dtos.UserDto;

public interface IUserService {
    UserDto register(SignUpDto signUpDto);

    UserDto login(CredentialsDto credentialsDto);

    UserDto getUser(final Long id);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto getCurrentUser();
}
