package com.ormi.storywave.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserUserId(String userId, Pageable pageable);


    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countCommentsByPostId(@Param("postId") Long postId);

    Optional<Post> findByBoard_PostTypeIdAndId(Long postTypeId, Long id);

    List<Post> findByTitleContaining(String titleKeyword);

    @Transactional
    @Modifying
    @Query("update Post p set p.thumbs = p.thumbs + 1 where p.id = ?1")
    Integer plusThumbs(Long id);

    @Transactional
    @Modifying
    @Query("update Post p set p.thumbs = p.thumbs-1 where p.id = ?1")
    int minusThumbs(Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.board.postTypeId = :postTypeId")
    List<Post> findByPostTypeIdWithUser(@Param("postTypeId") Long postTypeId);

    // 제목 또는 내용에 검색어가 포함된 게시물 조회
    //List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);
}
