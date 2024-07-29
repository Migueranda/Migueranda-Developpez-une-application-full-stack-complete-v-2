
package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.model.dtos.CommentDto;
import com.openclassrooms.mddapi.model.entities.CommentEntity;
import java.util.List;

public interface CommentMapper extends EntityMapper<CommentDto, CommentEntity> {
    CommentEntity toEntity(CommentDto dto);
    CommentDto toDto(CommentEntity entity);
    List<CommentEntity> toEntity(List<CommentDto> dtoList);
    List<CommentDto> toDto(List<CommentEntity> entityList);
}
