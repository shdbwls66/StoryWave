package com.example.storywave.Service;

import com.example.storywave.Dto.BoardDto;
import com.example.storywave.Dto.CategoryDto;
import com.example.storywave.Dto.PostDto;
import com.example.storywave.Dto.PostListDto;
import com.example.storywave.Entity.Board;
import com.example.storywave.Entity.Category;
import com.example.storywave.Entity.Image;
import com.example.storywave.Entity.Post;
import com.example.storywave.Repository.BoardRepository;
import com.example.storywave.Repository.CategoryRepository;
import com.example.storywave.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BoardRepository boardRepository;

    public List<PostListDto> getPostSummaries(Long post_type_id) {
        List<Post> posts = postRepository.findAll(); // 모든 게시글을 가져옵니다.

        return posts.stream()
                .filter(post ->
                        post.getCategories().stream().anyMatch(category ->
                                category.getBoard().getPostTypeId().equals(0L) ||
                                        category.getBoard().getPostTypeId().equals(post_type_id)
                        )
                )
                .map(post -> {
                    Long commentCount = postRepository.countCommentsByPostId(post.getId()); // 댓글 수 계산
                    Set<CategoryDto> categoryDtos = post.getCategories().stream()
                            .map(category -> new CategoryDto(
                                    category.getId(),
                                    new BoardDto(category.getBoard().getPostTypeId(), category.getBoard().getViewPost()), // BoardDto 생성
                                    category.getName()))
                            .collect(Collectors.toSet());
                    return new PostListDto(
                                                post.getId(),
                                                post.getTitle(),
                                                post.getUpdatedAt(),
                                                post.getThumbs(),
                            categoryDtos, // 댓글 수를 설정합니다.
                            commentCount // 올바른 타입으로 설정
                                        );
                })
                .collect(Collectors.toList());
    }

    public Post createPost(Post post, MultipartFile[] imageFiles, List<String> categoryNames, Long post_type_id, Integer thumbs) {
        // Board 설정
        Board board = boardRepository.findByPostTypeId(post_type_id).orElseGet(() -> {
            Board newBoard = new Board();
            newBoard.setPostTypeId(post_type_id);
            // 새로운 Board 생성 시 필요한 다른 필드들도 설정합니다.
            // 예: newBoard.setName("Default Board Name");
            // 필요에 따라 다른 필드들을 설정합니다.
            return boardRepository.save(newBoard);
        });
        post.setBoard(board);

        // Thumbs 설정
        post.setThumbs(thumbs != null ? thumbs : 0);

        // Categories 설정
        Set<Category> categories = new HashSet<>();
        for (String categoryName : categoryNames) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName);
                        newCategory.setBoard(board); // 카테고리에도 Board 설정
                        return categoryRepository.save(newCategory);
                    });
            categories.add(category);
        }
        post.setCategories(categories);

        // 이미지 파일 처리 로직 추가 (null 체크 포함)
        if (imageFiles != null) {
            saveImages(imageFiles, post);
        }

        return postRepository.save(post);
    }

    private void saveImages(MultipartFile[] imageFiles, Post post) {
        if (imageFiles == null || imageFiles.length == 0) {
            // 이미지 파일이 없는 경우, 로그를 남기거나 필요한 처리를 수행
            System.out.println("No image files uploaded for post: " + post.getId());
            return;
        }

        // 업로드 디렉터리 절대 경로 설정
        File uploadDir = new File("C:/path/to/uploads/");
        if (!uploadDir.exists()) {
            if (uploadDir.mkdirs()) {
                System.out.println("Created directory: " + uploadDir.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + uploadDir.getAbsolutePath());
                throw new RuntimeException("Failed to create directory for image upload");
            }
        }

        for (MultipartFile file : imageFiles) {
            if (file != null && !file.isEmpty()) {
                try {
                    // 파일 경로 설정
                    String filePath = uploadDir.getAbsolutePath() + "/" + file.getOriginalFilename();
                    System.out.println("Saving file to: " + filePath);
                    file.transferTo(new File(filePath));
                    Image image = new Image();
                    image.setUrl(filePath);
                    image.setPost(post);
                    post.getImages().add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Image saving failed: " + e.getMessage(), e);
                }
            }
        }
    }

    // 게시물 상세 조회
    public PostDto getPostByPostTypeIdAndPostId(Long post_type_id, Long postId) {
    return postRepository
            .findByBoard_PostTypeIdAndId(post_type_id, postId)
            .map(PostDto::fromPost)
            .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다."));
    }
}