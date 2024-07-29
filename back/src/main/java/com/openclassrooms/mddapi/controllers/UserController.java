package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.model.dtos.UserDto;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant les opérations sur les utilisateurs.
 * Fournit des points de terminaison pour récupérer et mettre à jour les utilisateurs.
 */
@RestController
public class UserController {
    @Autowired
   private UserService userService;

    /**
     * Récupère les informations d'un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à récupérer
     * @return une ResponseEntity contenant l'objet UserDto de l'utilisateur récupéré ou une réponse 404 si l'utilisateur n'est pas trouvé
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        try {
            UserDto userDto = userService.getUser(id);
            return ResponseEntity.ok(userDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param id l'identifiant de l'utilisateur à mettre à jour
     * @param userDto l'objet de transfert de données contenant les informations mises à jour de l'utilisateur
     * @return une ResponseEntity contenant l'objet UserDto de l'utilisateur mis à jour
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @Valid @RequestBody UserDto userDto){
        UserDto updatedUserEntity = userService.updateUser(Long.valueOf(id), userDto);
        return ResponseEntity.ok(updatedUserEntity);
    }
}
