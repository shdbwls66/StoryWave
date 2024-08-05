package com.ormi.storywave.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Integer> {

    Optional<Ban> findByUser_UserId(String userId);
}
