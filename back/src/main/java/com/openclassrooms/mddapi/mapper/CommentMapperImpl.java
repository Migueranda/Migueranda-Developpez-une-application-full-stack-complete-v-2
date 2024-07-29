package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.model.dtos.CommentDto;
import com.openclassrooms.mddapi.model.entities.CommentEntity;
import com.openclassrooms.mddapi.model.entities.PostEntity;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentEntity toEntity(CommentDto dto) {
        if (dto == null) {
            return null;
        }

        CommentEntity comment = new CommentEntity();
        comment.setId(dto.getId());
        comment.setDescription(dto.getDescription());
        comment.setDate(dto.getDate());

        if (dto.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(dto.getUserId());
            comment.setUser(user);
        }
        if (dto.getPostId() != null) {
            PostEntity post = new PostEntity();
            post.setId(dto.getPostId());
            comment.setPost(post);
        }

        return comment;
    }

    @Override
    public CommentDto toDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        return CommentDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .description(entity.getDescription())
                .date(entity.getDate())
                .postId(entity.getPost() != null ? entity.getPost().getId() : null)
                .build();
    }

    @Override
    public List<CommentEntity> toEntity(List<CommentDto> dtoList) {
        if (dtoList == null) {
            return null;
        }

        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> toDto(List<CommentEntity> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
