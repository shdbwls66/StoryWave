package com.ormi.storywave.image;

import com.ormi.storywave.posts.Posts;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/** DTO for {@link Image} */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageDto implements Serializable {
  private Long id;
  private String url;
  private Posts post;
  private LocalDateTime uploaded_at;
}
