package com.ormi.storywave.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPosts_PostId(Long postId);



//  Optional<Comment> findByUsers_UserId(String userId);

  Optional<Comment> findByPosts_PostIdAndCommentId(Long postId, Long commentId);

}
