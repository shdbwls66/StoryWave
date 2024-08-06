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
  private String username;
  private String email;
  private String role;
  private boolean activeStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String banPeriod;
  private String banReason;


//  private List<CommentDto> comments = new ArrayList<>();
//  private List<PostDto> posts = new ArrayList<>();

  // Entity -> DTO
  public static UserDto fromUsers(User user) {
    UserDto userDto =
            UserDto.builder()
                    .userId(user.getUserId())
                    .password(user.getPassword())
                    .nickname(user.getNickname())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .activeStatus(user.isActiveStatus())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .banReason(user.getBanReason())
                    .banPeriod(user.getBanPeriod())
                    .build();
//    if (user.getComments() != null) {
//      usersDto.setComments(
//              user.getComments().stream().map(CommentDto::fromComment).collect(Collectors.toList()));
//    }
//    if (user.getPosts() != null) {
//      usersDto.setPosts(
//              user.getPosts().stream().map(PostDto::fromPost).collect(Collectors.toList()));
//    }
    return userDto;
  }

  // DTO -> Entity
  public User toUsers() {
    User user = new User();
    user.setUserId(this.userId);
    user.setPassword(this.password);
    user.setNickname(this.nickname);
    user.setUsername(this.username);
    user.setEmail(this.email);
    user.setRole(this.role);
    user.setActiveStatus(this.activeStatus);
    user.setCreatedAt(this.createdAt);
    user.setUpdatedAt(this.updatedAt);
    user.setBanReason(this.banReason);
    user.setBanPeriod(this.banPeriod);

//    if (this.comments != null) {
//      this.comments.forEach(
//              commentDto -> {
//                Comment comment = commentDto.toComment();
//                user.getComments().add(comment);
//              });
//    }
//    if (this.posts != null) {
//      this.posts.forEach(
//              postDto -> {
//                Post post = postDto.toPost();
//                user.getPosts().add(post);
//              });
//    }
    return user;
  }

  public Integer getBanPeriodAsInteger() {
    return extractNumberFromString(banPeriod);
  }



  private Integer extractNumberFromString(String input) {
    if (input == null || input.isEmpty()) {
      return null;
    }
    try {
      // 문자열에서 숫자만 추출
      return Integer.parseInt(input.replaceAll("[^0-9]", ""));
    } catch (NumberFormatException e) {
      // 변환 실패 시 null 반환
      return null;
    }
  }
}
