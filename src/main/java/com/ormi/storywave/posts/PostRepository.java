package com.ormi.storywave.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countCommentsByPostId(@Param("postId") Long postId);

    Optional<Post> findByBoard_PostTypeIdAndId(Long postTypeId, Long id);

    List<Post> findByTitleContaining(String keyword);

    @Transactional
    @Modifying
    @Query("update Post p set p.thumbs = p.thumbs + 1 where p.id = ?1")
    Integer plusThumbs(Long id);

    @Transactional
    @Modifying
    @Query("update Post p set p.thumbs = p.thumbs-1 where p.id = ?1")
    int minusThumbs(Long id);


}
