package com.ormi.storywave.posts;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @Column
    private Integer post_type_id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @Column
    private Integer thumbs;

    @Column
    private Integer comment;

    @OneToMany(mappedBy = "posts")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPosts(this);
    }

    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setPosts(null);
    }

}
