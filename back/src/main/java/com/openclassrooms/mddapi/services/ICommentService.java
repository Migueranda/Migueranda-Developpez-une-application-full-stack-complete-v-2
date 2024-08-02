package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dtos.CommentDto;
import com.openclassrooms.mddapi.model.entities.CommentEntity;

import java.util.List;

public interface ICommentService {
    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto convertToDto(CommentEntity entity);

    CommentDto addComment(Long postId, CommentDto commentDto);
}
