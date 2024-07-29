package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
 Optional<UserEntity> findByEmail(String email);

 Optional<UserEntity> findById(Integer userId);


}
