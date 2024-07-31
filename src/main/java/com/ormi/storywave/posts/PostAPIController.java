package com.ormi.storywave.posts;

import com.ormi.storywave.posts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/api/post")
public class PostAPIController {
  private final PostService postService;

  @Autowired
  public PostAPIController(PostService postService) {
    this.postService = postService;
  }

  // 모든 포스트 조회
//  @GetMapping()
//  public ResponseEntity<List<PostDto>> getAllPosts() {
//    List<PostDto> posts = postService.getAllPosts();
//    return ResponseEntity.ok(posts);
//  }

  // 특정 id의 포스트 상세 조회
//  @GetMapping("/{postId}")
//  public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long postId) {
//    return postService
//        .getPostById(postId)
//        .map(ResponseEntity::ok)
//        .orElse(ResponseEntity.notFound().build());
//  }

  // 특정 제목 키워드로 검색
//  @GetMapping("/search/title")
//  public ResponseEntity<List<PostDto>> getPostsByTitleContaining(
//      @RequestParam("keyword") String keyword) {
//    List<PostDto> posts = postService.getPostsByTitleContaining(keyword);
//    return ResponseEntity.ok(posts);
//  }

  // 특정 게시물 수정
//  @PutMapping("/{postId}")
//  public ResponseEntity<PostDto> updatePost(
//      @PathVariable("postId") Long id,
//      @RequestBody PostDto updatePostDto,
//      @RequestParam("userId") String userId) {
//    return postService
//        .updatePost(id, updatePostDto, userId)
//        .map(ResponseEntity::ok)
//        .orElse(ResponseEntity.notFound().build());
//  }

//  @DeleteMapping("{postId}")
//  public ResponseEntity<Void> deletePosts(
//      @PathVariable("postId") Long id, @RequestParam("userId") String userId) {
//    boolean deleted = postService.deletePosts(id, userId);
//    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
//  }

  @PostMapping("/{post_type_id}")
  public ResponseEntity<PostDto> createPost(
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam("categories") List<String> categoryNames,
      @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
      @RequestParam(value = "thumbs", required = false) Integer thumbs,
      @PathVariable("post_type_id") Long post_type_id) {

    Posts post = new Posts();
    post.setTitle(title);
    post.setContent(content);

    // dto에서 엔티티로 변환
    Posts createdPost = postService.createPost(post, imageFiles, categoryNames, post_type_id, thumbs);

    // 엔티티에서 Dto로 변환
    //      PostDto postDto = PostMapper.INSTANCE.postToPostDto(createdPost);
    PostDto postDto = PostDto.fromPost(createdPost);
    return ResponseEntity.ok(postDto);
  }

  @GetMapping("/{post_type_id}")
  public ResponseEntity<List<PostListDto>> getPostSummaries(@PathVariable Long post_type_id) {
    List<PostListDto> postSummaries = postService.getPostSummaries(post_type_id);
    return ResponseEntity.ok(postSummaries);
  }
}
