package com.example.storywave.Dto;

import com.example.storywave.Entity.Comment;
import com.example.storywave.Entity.Post;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Comment}
 */
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class CommentDto implements Serializable {
    Long id;
    Post post;
    UserDto user_id;
    String content;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}