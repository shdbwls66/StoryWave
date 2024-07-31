package com.ormi.storywave.board;

import com.ormi.storywave.comment.Comment;
import com.ormi.storywave.comment.CommentDto;
import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.Posts;
import lombok.*;
import java.io.Serializable;
import java.util.stream.Collectors;

/**
 * DTO for {@link Board}
 */

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardDto implements Serializable {
  private Long postTypeId;
  private Integer viewPost;
}
