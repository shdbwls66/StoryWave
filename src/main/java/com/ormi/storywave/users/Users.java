package com.ormi.storywave.users;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.posts.Posts;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Getter
@Setter
@Builder
//@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @Column(name ="user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String role;

    @Column(name = "active_status", nullable = false)
    private boolean activeStatus;

    //    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    @OneToMany(mappedBy = "users")
    private List<Comment> comments = new ArrayList<>();

    @Column
    @OneToMany(mappedBy = "users")
    private List<Posts> posts = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUsers(this);
    }
    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setUsers(null);
    }

    public void addPost(Posts post) {
        posts.add(post);
        post.setUsers(this);
    }

    public void removePost(Posts post){
        posts.remove(post);
        post.setUsers(null);
    }

}
