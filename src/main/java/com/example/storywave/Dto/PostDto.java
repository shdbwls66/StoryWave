package com.example.storywave.Dto;

import com.example.storywave.Entity.Post;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link Post}
 */
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class PostDto implements Serializable {
    Long id;
    BoardDto board;
    String title;
    String content;
    UserDto user;
    LocalDateTime created_at;
    LocalDateTime updated_at;
    Integer thumbs;
    List<ImageDto> images;
    Set<CategoryDto> categories;
}