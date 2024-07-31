package com.ormi.storywave.users;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.comment.CommentDto;
import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.Posts;
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
public class UsersDto implements Serializable {
  private Integer id;
  private String username;
  private String password;
  private String nickname;
  private String email;
  private String role;
  private String active_status;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
  private List<CommentDto> comments = new ArrayList<>();
  private List<PostDto> posts = new ArrayList<>();

  public static UsersDto fromUsers(Users users) {
    UsersDto usersDto =
        UsersDto.builder()
            .id(users.getId())
            .username(users.getUsername())
            .password(users.getPassword())
            .nickname(users.getNickname())
            .email(users.getEmail())
            .role(users.getRole())
            .active_status(users.getActive_status())
            .created_at(users.getCreated_at())
            .updated_at(users.getUpdated_at())
            .build();
    if (users.getComments() != null) {
      usersDto.setComments(
          users.getComments().stream().map(CommentDto::fromComment).collect(Collectors.toList()));
    }
    if (users.getPosts() != null) {
      usersDto.setPosts(
          users.getPosts().stream().map(PostDto::fromPost).collect(Collectors.toList()));
    }
    return usersDto;
  }

  public Users toUsers() {
    Users users = new Users();
    users.setId(this.id);
    users.setUsername(this.username);
    users.setPassword(this.password);
    users.setNickname(this.nickname);
    users.setEmail(this.email);
    users.setRole(this.role);
    users.setActive_status(this.active_status);
    users.setCreated_at(this.created_at);
    users.setUpdated_at(this.updated_at);

    if (this.comments != null) {
      this.comments.forEach(
          commentDto -> {
            Comment comment = commentDto.toComment();
            users.getComments().add(comment);
          });
    }
    if (this.posts != null) {
      this.posts.forEach(
          postDto -> {
            Posts post = postDto.toPost();
            users.getPosts().add(post);
          });
    }
    return users;
  }
}
