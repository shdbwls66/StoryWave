package com.ormi.storywave.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByPosts_PostId(Integer postId);



  Optional<Comment> findByUsers_UserId(String userId);

  Optional<Comment> findByPosts_PostIdAndCommentId(Integer postId, Integer commentId);

}
