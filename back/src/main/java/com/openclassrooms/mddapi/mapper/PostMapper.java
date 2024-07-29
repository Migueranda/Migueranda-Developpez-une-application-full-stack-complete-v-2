package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.model.dtos.PostDto;
import com.openclassrooms.mddapi.model.entities.PostEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper implements EntityMapper<PostDto, PostEntity> {

    @Override
    public PostEntity toEntity(PostDto dto) {
        if (dto == null) {
            return null;
        }
        PostEntity postEntity = new PostEntity();
        postEntity.setId(dto.getId());
        postEntity.setTitle(dto.getTitle());
        postEntity.setDate(dto.getDate());
        postEntity.setDescription(dto.getDescription());

        return postEntity;
    }

    @Override
    public PostDto toDto(PostEntity entity) {
        if (entity == null) {
            return null;
        }
        return PostDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .description(entity.getDescription())
                .userId(Math.toIntExact(entity.getUser() != null ? entity.getUser().getId() : null))
                .themeId(entity.getTheme() != null ? entity.getTheme().getId() : null)
                .build();
    }

    @Override
    public List<PostEntity> toEntity(List<PostDto> dtoList) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> toDto(List<PostEntity> entityList) {
        if (entityList == null) {
            return null;
        }
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
