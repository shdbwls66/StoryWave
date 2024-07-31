package com.ormi.storywave.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<Users> findByPosts_PostId(Long postId);

    Optional<Users> findByComments_CommentId(Long commentId);

    Optional<Users> findByUserId(String userId);
}
