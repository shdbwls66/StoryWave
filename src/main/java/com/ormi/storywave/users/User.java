package com.ormi.storywave.users;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.UserPostLike;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Getter
@Setter
@Builder
// @EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String nickname;

  @Column(nullable = false)
  private String username; // 추가

  @Column(nullable = false)
  private String role;

  @Column(name = "active_status", nullable = false)
  private boolean activeStatus;

  //    @CreatedDate
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column
  @OneToMany(mappedBy = "user")
  private List<Comment> comments = new ArrayList<>();

  @Column
  @OneToMany(mappedBy = "user")
  private List<Post> posts = new ArrayList<>();

  @Column
  @OneToMany(mappedBy = "user")
  private List<UserPostLike> likes = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  // 유저 댓글 목록 관리
  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setUser(this);
  }

  // 유저 댓글 목록 관리
  public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setUser(null);
  }

  // 유저 포스트 목록 관리
  public void addPost(Post post) {
    posts.add(post);
    post.setUser(this);
  }

  // 유저 포스트 목록 관리
  public void removePost(Post post) {
    posts.remove(post);
    post.setUser(null);
  }
}