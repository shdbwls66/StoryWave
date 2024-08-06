package com.ormi.storywave.board;

import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.PostRepository;
import com.ormi.storywave.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private PostRepository postRepository;

    // 검색 서비스 메서드 추가
    public List<PostListDto> searchPosts(String keyword) {
        // 키워드가 null이거나 빈 문자열이면 빈 리스트 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 게시물 검색
        List<Post> searchResults = postRepository.findByTitleContaining(keyword);

        // 검색 결과를 PostListDto로 변환
        return searchResults.stream()
                .map(post -> {
                    Long commentCount = postRepository.countCommentsByPostId(post.getId());
                    Set<CategoryDto> categoryDtos = post.getCategories().stream()
                            .map(category -> new CategoryDto(
                                    category.getId(),
                                    new BoardDto(category.getBoard().getPostTypeId(), category.getBoard().getViewPost()),
                                    category.getName()
                            ))
                            .collect(Collectors.toSet());

                    User user = post.getUser();
                    String userId = (user != null) ? user.getUserId() : "Unknown";
                    String nickname = (user != null) ? user.getNickname() : "Unknown";

                    return new PostListDto(
                            post.getId(),
                            post.getTitle(),
                            post.getUpdatedAt(),
                            post.getThumbs(),
                            categoryDtos,
                            commentCount,
                            userId,   // UserId 정보 추가
                            nickname  // Nickname 정보 추가
                    );
                })
                .collect(Collectors.toList());
    }
}
