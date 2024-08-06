package com.ormi.storywave.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByPosts_Id(Long id);

    Optional<User> findByComments_CommentId(Long commentId);

    Optional<User> findByUserId(String userId);

    Optional<User> findByRole (String role);

    Optional<User> findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);

}
