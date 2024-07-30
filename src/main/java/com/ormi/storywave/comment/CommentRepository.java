package com.ormi.storywave.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

  List<Comment> findByPosts_Id(Integer id);

  Optional<Comment> findByPosts_IdAndId(Integer postId, Integer id);

  Optional<Comment> findByUsers_Id(Integer userId);
}
