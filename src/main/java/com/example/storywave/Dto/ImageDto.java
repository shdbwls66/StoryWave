package com.example.storywave.Dto;

import com.example.storywave.Entity.Image;
import com.example.storywave.Entity.Post;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Image}
 */
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class ImageDto implements Serializable {
    Long id;
    String url;
    Post post;
    LocalDateTime uploaded_at;
}