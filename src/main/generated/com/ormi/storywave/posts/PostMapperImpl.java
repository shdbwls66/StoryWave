package com.ormi.storywave.posts;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-05T03:42:09+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDto postToPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto postDto = new PostDto();

        return postDto;
    }
}
