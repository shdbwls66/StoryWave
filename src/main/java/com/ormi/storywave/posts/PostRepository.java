package com.ormi.storywave.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Posts, Integer> {
    List<Posts> findByTitleContaining(String keyword);

    Optional<Posts> findByUsers_IdAndId(Integer usersId, Integer postId);

}
