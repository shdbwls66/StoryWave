package com.example.storywave.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.storywave.Entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countCommentsByPostId(@Param("postId") Long postId);
}