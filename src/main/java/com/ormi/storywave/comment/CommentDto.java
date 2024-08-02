package com.ormi.storywave.comment;

import java.time.LocalDateTime;
import lombok.*;


//@Value
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private Long postId;
    private String userId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentDto fromComment(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getUserId())
                .nickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public Comment toComment() {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setContent(content);
        comment.setCreatedAt(createdAt);
        comment.setUpdatedAt(updatedAt);
        return comment;
    }
}
