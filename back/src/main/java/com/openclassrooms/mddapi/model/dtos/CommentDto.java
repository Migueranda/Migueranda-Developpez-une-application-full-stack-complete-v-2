package com.openclassrooms.mddapi.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.openclassrooms.mddapi.model.entities.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Long id;
    private String description;
    private Date date;
    private Long userId;
    private Long postId;
    private String userName;

    public static CommentDto convertToDto(CommentEntity commentEntity) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentEntity.getId());
        commentDto.setUserId(commentEntity.getUser().getId());
        commentDto.setDescription(commentEntity.getDescription());
        commentDto.setPostId(commentDto.getPostId());
        commentDto.setDate(commentEntity.getDate());

        return commentDto;
    }

}
