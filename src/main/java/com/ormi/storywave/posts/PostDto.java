package com.ormi.storywave.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/** DTO for {@link Post} */
// @Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostDto implements Serializable {
  private Long postId;
  private Long postTypeId;
  private String title;
  private String content;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDateTime updatedAt;

  private Integer commentCount;
  private Integer thumbs;

  public static PostDto fromPost(Post post) {

    return PostDto.builder()
        .postId(post.getId())
        .postTypeId(post.getBoard().getPostTypeId())
        .title(post.getTitle())
        .content(post.getContent())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .commentCount((post.getCommentCount()))
        .thumbs(post.getThumbs())
        .build();
  }

  public Post toPost() {
    Post post = new Post();
    post.setId(this.postId);
    post.setTitle(this.title);
    post.setContent(this.content);
    post.setCreatedAt(this.createdAt);
    post.setUpdatedAt(this.updatedAt);
    post.setCommentCount(this.commentCount);
    post.setThumbs(this.thumbs);

    return post;
  }
}
