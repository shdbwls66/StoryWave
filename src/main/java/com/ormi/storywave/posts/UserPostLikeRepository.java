package com.ormi.storywave.posts;

import com.ormi.storywave.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {

  boolean existsByPostIdAndUser_UserId(Long postId, String userId);

  Optional<UserPostLike> findByPostAndUser(Post post, User user);

  void deleteByPost(Post post);
}
