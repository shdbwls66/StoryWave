package com.ormi.storywave.comment;

import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostRepository;
import com.ormi.storywave.users.User;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
          CommentRepository commentRepository,
          PostRepository postRepository,
          UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public CommentDto createComment(CommentDto commentDto, Long postId, String userId) {
    Post posts =
            postRepository
                    .findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

    User users =
            userRepository
                    .findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

    Comment comment = commentDto.toComment();
    comment.setCreatedAt(LocalDateTime.now());
    comment.setPost(posts);

    Comment savedComment = commentRepository.save(comment);
    posts.addComment(savedComment);
    users.addComment(savedComment);

    return CommentDto.fromComment(savedComment);
  }

  // 모든 댓글 조회
  @Transactional(readOnly = true)
  public List<CommentDto> getAllComments(Long postId) {
    return commentRepository.findByPost_Id(postId).stream()
            .map(CommentDto::fromComment)
            .collect(Collectors.toList());
  }

  public CommentDto getCommentById(Long commentId) {
    return commentRepository
            .findById(commentId)
            .map(CommentDto::fromComment)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
  }

  // 댓글 작성자만 수정 가능
  public Optional<CommentDto> updateComment(
//          Long postId, Long commentId, CommentDto commentDto, String userId, HttpSession session) {
          Long postId, Long commentId, CommentDto commentDto, String userId) {
//    String userId = session.getAttribute("userId");
    return commentRepository
            .findByPost_IdAndCommentId(postId, commentId)
            .filter(comment -> comment.getUser().getUserId().equals(userId))
            .map(
                    comment -> {
                      comment.setContent(commentDto.getContent());
                      comment.setUpdatedAt(LocalDateTime.now());
                      return CommentDto.fromComment(commentRepository.save(comment));
                    });
  }

  // 댓글 작성자나, 운영자만 댓글 삭제 가능
//  public boolean deleteComment(Long postId, Long commentId, HttpSession session) {
  public boolean deleteComment(Long postId, Long commentId, String userId) {
    //    String userId = session.getAttribute("userId");
    Post posts = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    UserDto users =
            userRepository
                    .findByUserId(userId)
                    .map(UserDto::fromUsers)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
    return commentRepository
            .findByPost_IdAndCommentId(postId, commentId)
            .filter(
                    post -> post.getUser().getUserId().equals(userId) || users.getRole().equals("admin"))
            .map(
                    comment -> {
                      commentRepository.delete(comment);
                      return true;
                    })
            .orElse(false);
  }

  public Page<Comment> findPaginated(int page, int pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    return commentRepository.findAll(pageable);
  }

  public Integer commentCount(Long postId){
    return commentRepository.countCommentsByPost_Id(postId);
  }

  public void deleteCommentsByPostId(Long postId) {
    Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    commentRepository.deleteByPost(post);
  }
}
