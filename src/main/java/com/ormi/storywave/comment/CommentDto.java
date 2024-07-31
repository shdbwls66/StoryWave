package com.ormi.storywave.comment;

import java.time.LocalDateTime;

import com.ormi.storywave.posts.Posts;
import com.ormi.storywave.users.Users;
import com.ormi.storywave.users.UsersDto;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDto {
    private Integer comment_id;
    private Integer post_id;
    private Integer user_id;
    private String nickname;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static CommentDto fromComment(Comment comment) {
        return CommentDto.builder()
                .comment_id(comment.getId())
                .post_id(comment.getPosts().getId())
                .user_id(comment.getUsers().getId())
                .nickname(comment.getUsers().getNickname())
                .content(comment.getContent())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .build();
    }

    public Comment toComment() {
        Comment comment = new Comment();
        comment.setId(comment_id);
        comment.setContent(content);
        comment.setCreated_at(created_at);
        comment.setUpdated_at(updated_at);
        return comment;
    }
}
