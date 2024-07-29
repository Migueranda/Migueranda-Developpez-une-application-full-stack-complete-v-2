package com.openclassrooms.mddapi.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.openclassrooms.mddapi.model.entities.PostEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private Long id;
    private String title;
    private String description;
    private Long themeId;
    private Integer userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Timestamp date;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static PostDto convertToDto(PostEntity postEntity) {
        if (postEntity == null) {
            return null;
        }
        return PostDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .date(postEntity.getDate())
                .description(postEntity.getDescription())
                .userId(Math.toIntExact(postEntity.getUser() != null ? postEntity.getUser().getId() : null))
                .themeId(postEntity.getTheme() != null ? postEntity.getTheme().getId() : null)
                .build();
    }

}
