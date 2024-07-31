package com.ormi.storywave.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ormi.storywave.posts.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // URL or file path of the image
    private String url;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    @Column(updatable = false)
    private LocalDateTime uploaded_at;

    @PrePersist
    protected void onCreate() {
        uploaded_at = LocalDateTime.now();
    }
}