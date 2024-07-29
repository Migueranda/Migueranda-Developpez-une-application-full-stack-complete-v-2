package com.openclassrooms.mddapi.repositories;
import com.openclassrooms.mddapi.model.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
   List<CommentEntity> findByPostId(Long postId);
}