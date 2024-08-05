package com.ormi.storywave.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.comment.CommentDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

  private List<CommentDto> comments;

  private Integer thumbs;

  public static PostDto fromPost(Post post) {

    PostDto postDto = PostDto.builder()
            .postId(post.getId())
            .postTypeId(post.getBoard().getPostTypeId())
            .title(post.getTitle())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .thumbs(post.getThumbs())
            .build();

    if (post.getComments() != null) {
      postDto.setComments(
              post.getComments()
                      .stream()
                      .map(CommentDto::fromComment)
                      .collect(Collectors.toList()));
    }
    return postDto;
  }

  public Post toPost() {
    Post post = new Post();
    post.setId(this.postId);
    post.setTitle(this.title);
    post.setContent(this.content);
    post.setCreatedAt(this.createdAt);
    post.setUpdatedAt(this.updatedAt);
    post.setThumbs(this.thumbs);

    if (this.comments != null) {
      this.comments.forEach(
              commentDto -> {
                Comment comment = commentDto.toComment();
                post.getComments().add(comment);
              });
    }

    return post;
  }
}