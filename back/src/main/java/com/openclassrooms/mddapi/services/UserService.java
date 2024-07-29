package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.AppException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.dtos.CredentialsDto;
import com.openclassrooms.mddapi.model.dtos.SignUpDto;
import com.openclassrooms.mddapi.model.dtos.UserDto;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

/**
 * Classe de service pour la gestion des utilisateurs.
 *Fournit des méthodes pour l'inscription, la connexion, la récuperation et la mise à jour des données des utilisateurs.
 */

@Data
@Service
public class UserService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, SubjectRepository subjectRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Enregistrer un nouvel utilisateur dans la base de données.
     *
     * @param signUpDto l'objet de transfert de données contenant les informations de l'inscription
     * @return l'objet de transfert de données représentant l'utilisateur enregistré.
     * @throws  AppException si un utilisateur avec le même email existe déjà.
     */
    public UserDto register(SignUpDto signUpDto){
        Optional<UserEntity> oUser = userRepository.findByEmail(signUpDto.email());
        if (oUser.isPresent()){
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));

        UserEntity savedUser = userRepository.save(user);
        System.out.println("User registered successfully with email: " + signUpDto.email());
        return userMapper.toDto(savedUser);
    }

    /**
     * Authentifie un utilisateur et permet sa connexion
     *
     * @param credentialsDto l'objet de transfert de données contenant les identifiants de connexion.
     * @return l'objet de transfert de données représentant l'utilisateur connecté.
     * @throws AppException si l'adresse email n'existe pas ou le mot de passe est incorrect.
     * */
    public UserDto login(CredentialsDto credentialsDto){
        UserEntity user = userRepository.findByEmail(credentialsDto.email())
                .orElseThrow(() -> new AppException("Unkown user", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword())) {
            return userMapper.toDto(user);
        }
        throw new AppException("Invalide password",HttpStatus.BAD_REQUEST);
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à récupérer
     * @return l'objet de transfert de données représentant l'utilisateur.
     * @throws EntityNotFoundException si l'utilisateur n'est pas trouvé.
     */
    public UserDto getUser(final Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserEntity not found for id: " + id));
        List<Subject> subjects = subjectRepository.findByUsers_Id(id);

        return userMapper.toDto(userEntity, subjects);
    }

    /**
     * Met à jour les informations dans un utilisateur dans la base de données.
     *
     * @param id l'identifiant de l'utilisateur à mettre à jour
     * @param userDto l'objet de transfert de données contenant les informations de l'utilisateur mise à jour.
     * @return l'objet de transfert de données représentant l'utilisateur à jour.
     *  @throws EntityNotFoundException si l'utilisateur n'est pas trouvé
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        UserEntity existingUserEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserEntity not found for id: " + id));

        existingUserEntity.setEmail(userDto.getEmail());
        existingUserEntity.setUserName(userDto.getUserName());

        if (!userDto.getPassword().equals(existingUserEntity.getPassword())) {
            existingUserEntity.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        }

        UserEntity updatedUserEntity = userRepository.save(existingUserEntity);
        return userMapper.toDto(updatedUserEntity);
    }
}




