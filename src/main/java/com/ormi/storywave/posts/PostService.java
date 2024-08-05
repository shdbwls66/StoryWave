package com.ormi.storywave.posts;

import com.ormi.storywave.board.*;
import com.ormi.storywave.users.User;
import com.ormi.storywave.users.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PostService {

  @Autowired private PostRepository postRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private BoardRepository boardRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private UserPostLikeRepository userPostLikeRepository;

  @Autowired
  private HttpSession session;

  @Value("${file.upload-dir}")
  private String uploadDir;


  // 페이지 번호, 크기를 기반으로 페이지네이션된 게시물 반환 메서드
  public Page<Post> findPaginated(int page, int pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    return postRepository.findAll(pageable);
  }

  public List<PostListDto> getPostSummaries(Long post_type_id) {
    // 사용자 정보가 포함된 게시글 리스트를 조회합니다.
    List<Post> posts = postRepository.findAll(); // 모든 게시글을 가져옵니다.

    return posts.stream()
            .filter(post ->
                    post.getCategories().stream().anyMatch(category ->
                            category.getBoard().getPostTypeId().equals(0L) ||
                                    category.getBoard().getPostTypeId().equals(post_type_id)
                    )
            )
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
  public Post createPost(
          Post post,
          MultipartFile[] imageFiles,
          List<String> categoryNames,
          Long post_type_id,
          Integer thumbs,String userid) {  // User 파라미터 추가


    User user = userRepository.findById(userid)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userid));

    // Board 설정
    Board board = boardRepository.findByPostTypeId(post_type_id)
            .orElseGet(() -> {
              Board newBoard = new Board();
              newBoard.setPostTypeId(post_type_id);
              newBoard.setViewPost(0); // view_post 필드를 0으로 설정합니다.
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

    // User 설정
    post.setUser(user); // User 객체 설정

    if (post.getContent() != null) {
      String originalContent = post.getContent();
      System.out.println("Original content length: " + originalContent.length());

      String updatedContent = extractAndSaveImages(originalContent, post);
      post.setContent(updatedContent);

      System.out.println("Updated content length: " + updatedContent.length());
    }

    Post savedPost = postRepository.save(post);
    System.out.println("Saved post content length: " + savedPost.getContent().length());
    return savedPost;
  }

  private String extractAndSaveImages(String content, Post post) {
    String imagePattern = "<img[^>]+src\\s*=\\s*['\"]data:image/[^;]+;base64,([^'\"]+)['\"][^>]*>";
    Pattern pattern = Pattern.compile(imagePattern);
    Matcher matcher = pattern.matcher(content);
    StringBuilder updatedContent = new StringBuilder();

    int lastMatchEnd = 0;
    while (matcher.find()) {
      String base64Image = matcher.group(1);

      String imagePath = saveImage(base64Image, post);

      // 로컬 파일 시스템 경로 대신 웹 서버에서 접근 가능한 URL 경로로 설정
      String fileName = new File(imagePath).getName(); // 이미지 파일명 추출
      updatedContent.append(content, lastMatchEnd, matcher.start());
      updatedContent.append("<img src=\"").append("/images/" + fileName).append("\" />");
      lastMatchEnd = matcher.end();
    }
    updatedContent.append(content.substring(lastMatchEnd));

    return updatedContent.toString();
  }

  private String saveImage(String base64Image, Post post) {
    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
    System.out.println("Decoded image size: " + imageBytes.length + " bytes");

    String fileName = UUID.randomUUID().toString() + ".jpg";
    File file = new File(uploadDir + fileName);

    try {
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
      }

      FileUtils.writeByteArrayToFile(file, imageBytes);
      System.out.println("Image saved to file: " + file.getAbsolutePath());

      Image image = new Image();
      image.setUrl(file.getAbsolutePath());
      image.setPost(post);
      post.getImages().add(image);

      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Failed to save image: " + e.getMessage());
      throw new RuntimeException("Failed to save image", e);
    }
  }

  @Transactional
  public Optional<PostDto> updatePost(Long postTypeId, Long postId, PostDto updatePostDto) {
    return postRepository
        .findByBoard_PostTypeIdAndId(postTypeId, postId)
        .map(
            post -> {
              post.setTitle(updatePostDto.getTitle());
              post.setContent(updatePostDto.getContent());
              post.setUpdatedAt(LocalDateTime.now());
              return PostDto.fromPost(postRepository.save(post));
            });
  }

  @Transactional
  public boolean deletePosts(Long postTypeId, Long postId) {
    return postRepository
        .findById(postId)
        .map(
            posts -> {
              postRepository.delete(posts);
              return true;
            })
        .orElse(false);
  }

  // 공감 누른 데이터가 있는지 여부 확인하는 메서드
  public boolean findPostLike(Long postId, String userId) {
    return userPostLikeRepository.existsByPostIdAndUser_UserId(postId, userId);
  }

  // 공감 여부 확인 후, false일 때(공감 데이터가 없을 때) 공감되도록 함
  public boolean saveLike(Long postId, String userId) {
    if (findPostLike(postId, userId)) {
      throw new IllegalArgumentException("이미 공감한 게시물입니다.");
    }
    User user =
        userRepository
            .findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));

    UserPostLike userPostLike = new UserPostLike();
    userPostLike.setPost(post);
    userPostLike.setUser(user);
    userPostLikeRepository.save(userPostLike);
    postRepository.plusThumbs(postId);

    return true;
  }

  // 게시물 상세 조회
  public PostDto getPostByPostTypeIdAndPostId(Long post_type_id, Long postId) {
    return postRepository
        .findByBoard_PostTypeIdAndId(post_type_id, postId)
        .map(PostDto::fromPost)
        .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다."));
  }
}
