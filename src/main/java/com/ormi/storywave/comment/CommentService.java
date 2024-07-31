package com.ormi.storywave.comment;

import com.ormi.storywave.posts.Posts;
import com.ormi.storywave.posts.PostRepository;
import com.ormi.storywave.users.UserRepository;
import com.ormi.storywave.users.Users;
import com.ormi.storywave.users.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Autowired
  public CommentService(
          CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public CommentDto createComment(CommentDto commentDto, Integer postId, String userId) {
    Posts posts =
            postRepository
                    .findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    Users users =
            userRepository
                    .findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
    Comment comment = commentDto.toComment();
    comment.setCreatedAt(LocalDateTime.now());
    comment.setPosts(posts);
    Comment savedComment = commentRepository.save(comment);
    posts.addComment(savedComment);
    users.addComment(savedComment);
    return CommentDto.fromComment(savedComment);
  }

  // 모든 댓글 조회
  @Transactional(readOnly = true)
  public List<CommentDto> getAllComments(Integer postId) {
    return commentRepository.findByPosts_PostId(postId).stream()
            .map(CommentDto::fromComment)
            .collect(Collectors.toList());
  }

  public CommentDto getCommentById(Integer commentId) {
    return commentRepository
            .findById(commentId)
            .map(CommentDto::fromComment)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
  }

  // 댓글 작성자만 수정 가능
  public Optional<CommentDto> updateComment(
          Integer postId, Integer commentId, CommentDto commentDto, String userId) {
    return commentRepository
            .findByPosts_PostIdAndCommentId(postId, commentId)
            .filter(comment -> comment.getUsers().getUserId().equals(userId))
            .map(
                    comment -> {
                      comment.setContent(commentDto.getContent());
                      comment.setUpdatedAt(LocalDateTime.now());
                      return CommentDto.fromComment(commentRepository.save(comment));
                    });
  }

  // 댓글 작성자나, 운영자만 댓글 삭제 가능
  public boolean deleteComment(Integer postId, Integer commentId, String userId) {
    UsersDto users =
            userRepository
                    .findById(userId)
                    .map(UsersDto::fromUsers)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
    return commentRepository
            .findByPosts_PostIdAndCommentId(postId, commentId)
            .filter(posts -> posts.getUsers().getUserId().equals(userId) || users.getRole().equals("admin"))
            .map(
                    comment -> {
                      commentRepository.delete(comment);
                      return true;
                    })
            .orElse(false);
  }
}
