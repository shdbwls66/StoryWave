package com.ormi.storywave.users;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.posts.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String role;

    @Column
    private String active_status;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

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
