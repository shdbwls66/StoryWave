package com.ormi.storywave.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class PostListDto {
    Long id;
    String title;
    LocalDateTime updated_at;
    Integer thumbs;
    Set<CategoryDto> categories;
    Long comments;
    private String userId;     // UserId 추가
    private String nickname;   // Nickname 추가
}
