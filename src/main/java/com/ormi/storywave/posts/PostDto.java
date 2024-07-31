package com.ormi.storywave.posts;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.comment.CommentDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostDto implements Serializable {
  private Long postId;
  private Long postTypeId;
  private String userId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer thumbs;
  private Integer commentsCount;
  private List<CommentDto> commentsList=new ArrayList<>();

  public static PostDto fromPost(Posts posts) {
    PostDto postDto =
            PostDto.builder()
                    .postId(posts.getPostId())
                    .postTypeId(posts.getPostTypeId())
                    .title(posts.getTitle())
                    .content(posts.getContent())
                    .createdAt(posts.getCreatedAt())
                    .updatedAt(posts.getUpdatedAt())
                    .thumbs(posts.getThumbs())
                    .build();

    if (posts.getComments() != null) {
      postDto.setCommentsList(
              posts.getComments().stream()
                      .map(CommentDto::fromComment)
                      .collect(Collectors.toList()));
    }

    if (posts.getUsers() != null) {
      postDto.setUserId(posts.getUsers().getUserId());
    } else {
      postDto.setUserId(null);
    }
    return postDto;
  }

  public Posts toPost() {
    Posts posts = new Posts();
    posts.setPostId(this.postId);
    posts.setPostTypeId(this.postTypeId);
    posts.setTitle(this.title);
    posts.setContent(this.content);
    posts.setCreatedAt(this.createdAt);
    posts.setUpdatedAt(this.updatedAt);
    posts.setThumbs(this.thumbs);

    if (this.commentsList != null) {
      this.commentsList.forEach(
              commentDto -> {
                Comment comment = new Comment();
                posts.addComment(comment);
              });
    }
//    if (this.user_id != null) {
//      setUser_id(posts.getUsers().getPostId());
//    } else {
//      setUser_id(null);
//    }

    return posts;
  }
}
