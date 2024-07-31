package com.ormi.storywave.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById(Integer userId);

    Optional<Users> findByPosts_Id(Integer postId);

    Optional<Users> findByComments_Id(Integer commentId);
}
