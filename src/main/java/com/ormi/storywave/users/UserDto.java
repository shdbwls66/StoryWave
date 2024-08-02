package com.ormi.storywave.users;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.comment.CommentDto;
import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostDto;
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
public class UserDto implements Serializable {
  private String userId;
  private String password;
  private String nickname;
  private String email;
  private String role;
  private boolean activeStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<CommentDto> comments = new ArrayList<>();
  private List<PostDto> posts = new ArrayList<>();

  // Entity -> DTO
  public static UserDto fromUsers(User users) {
    UserDto usersDto =
            UserDto.builder()
                    .userId(users.getUserId())
                    .password(users.getPassword())
                    .nickname(users.getNickname())
                    .email(users.getEmail())
                    .role(users.getRole())
                    .activeStatus(users.isActiveStatus())
                    .createdAt(users.getCreatedAt())
                    .updatedAt(users.getUpdatedAt())
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

  // DTO -> Entity
  public User toUsers() {
    User users = new User();
    users.setUserId(this.userId);
    users.setPassword(this.password);
    users.setNickname(this.nickname);
    users.setEmail(this.email);
    users.setRole(this.role);
    users.setActiveStatus(this.activeStatus);
    users.setCreatedAt(this.createdAt);
    users.setUpdatedAt(this.updatedAt);

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
                Post post = postDto.toPost();
                users.getPosts().add(post);
              });
    }
    return users;
  }
}
