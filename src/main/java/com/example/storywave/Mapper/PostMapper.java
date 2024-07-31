package com.example.storywave.Mapper;

import com.example.storywave.Dto.PostDto;
import com.example.storywave.Entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostDto postToPostDto(Post post);
}