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
  private Integer id;
  private Integer post_type_id;
  private Integer user_id;
  private String title;
  private String content;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
  private Integer thumbs;
  private Integer comments_count;
  private List<CommentDto> commentsList=new ArrayList<>();

  public static PostDto fromPost(Posts posts) {
    PostDto postDto =
        PostDto.builder()
            .id(posts.getId())
            .post_type_id(posts.getPost_type_id())
            .title(posts.getTitle())
            .content(posts.getContent())
            .created_at(posts.getCreated_at())
            .updated_at(posts.getUpdated_at())
            .thumbs(posts.getThumbs())
            .build();

    if (posts.getComments() != null) {
      postDto.setCommentsList(
          posts.getComments().stream()
                  .map(CommentDto::fromComment)
                  .collect(Collectors.toList()));
    }

    if (posts.getUsers() != null) {
      postDto.setUser_id(posts.getUsers().getId());
    } else {
      postDto.setUser_id(null);
    }
    return postDto;
  }

  public Posts toPost() {
    Posts posts = new Posts();
    posts.setId(this.id);
    posts.setPost_type_id(this.post_type_id);
    posts.setTitle(this.title);
    posts.setContent(this.content);
    posts.setCreated_at(this.created_at);
    posts.setUpdated_at(this.updated_at);
    posts.setThumbs(this.thumbs);

    if (this.commentsList != null) {
      this.commentsList.forEach(
          commentDto -> {
            Comment comment = new Comment();
            posts.addComment(comment);
          });
    }
//    if (this.user_id != null) {
//      setUser_id(posts.getUsers().getId());
//    } else {
//      setUser_id(null);
//    }

    return posts;
  }
}
