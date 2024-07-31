package com.example.storywave.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @Lob
    private String content;

    @Column(updatable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}