package com.ormi.storywave.posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostAPIController {
  private final PostsService postsService;

  @Autowired
  public PostAPIController(PostsService postsService) {
    this.postsService = postsService;
  }

  // 모든 포스트 조회
  @GetMapping()
  public ResponseEntity<List<PostDto>> getAllPosts() {
    List<PostDto> posts = postsService.getAllPosts();
    return ResponseEntity.ok(posts);
  }

  // 포스트 생성
  @PostMapping
  public ResponseEntity<PostDto> createPosts(@RequestBody PostDto postDto, @RequestParam("userId") Integer userId) {
    PostDto createdPosts = postsService.createPosts(postDto, userId);
    return new ResponseEntity<>(createdPosts, HttpStatus.CREATED);
  }

  // 특정 id의 포스트 상세 조회
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Integer postId) {
    return postsService
        .getPostById(postId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // 특정 제목 키워드로 검색
  @GetMapping("/search/title")
  public ResponseEntity<List<PostDto>> getPostsByTitleContaining(
      @RequestParam("keyword") String keyword) {
    List<PostDto> posts = postsService.getPostsByTitleContaining(keyword);
    return ResponseEntity.ok(posts);
  }

  // 특정 게시물 수정
  @PutMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(
      @PathVariable("postId") Integer id, @RequestBody PostDto updatePostDto, @RequestParam("userId") Integer userId) {
    return postsService
        .updatePost(id, updatePostDto, userId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{postId}")
  public ResponseEntity<Void> deletePosts(@PathVariable("postId") Integer id, @RequestParam("userId") Integer userId) {
    boolean deleted = postsService.deletePosts(id, userId);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
