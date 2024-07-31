package com.example.storywave.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.storywave.Entity.Board;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByPostTypeId(Long postTypeId);
}