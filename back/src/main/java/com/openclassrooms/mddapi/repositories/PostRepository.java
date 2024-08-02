package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.PostEntity;
import com.openclassrooms.mddapi.model.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    PostEntity save(PostEntity postEntity);
    Optional<PostEntity> findById(Long postId);
    List<PostEntity> findByThemeIn(List<Subject> subjects);
}