package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dtos.PostDto;
import com.openclassrooms.mddapi.model.entities.PostEntity;

import java.util.List;

public interface IPostService {
    List<PostDto> getAllPost(PostDto postDto, String sortBy, String order);

    PostDto createPost(PostDto postDto);

    PostDto getPostById(Long id);

    List<PostEntity> getPostsForUser(Long userId);
}
