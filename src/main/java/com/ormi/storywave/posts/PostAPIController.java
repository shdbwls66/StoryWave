package com.ormi.storywave.posts;

import com.ormi.storywave.board.PostListDto;
import com.ormi.storywave.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/post")
public class PostAPIController {
  private final PostService postService;
  private final UserService userService;

  @Autowired
  public PostAPIController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  @PostMapping("/{post_type_id}")
  public ResponseEntity<PostDto> createPost(
          @RequestParam("title") String title,
          @RequestParam("content") String content,
          @RequestParam("categories") List<String> categoryNames,
          @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
          @RequestParam(value = "thumbs", required = false) Integer thumbs,
          @PathVariable("post_type_id") Long post_type_id,
          @RequestParam("userid") String userid)
  {

    Post post = new Post();
    post.setTitle(title);
    post.setContent(content);


    // dto에서 엔티티로 변환
    Post createdPost = postService.createPost(post, imageFiles, categoryNames, post_type_id, thumbs,userid);

    // 엔티티에서 Dto로 변환
    PostDto postDto = PostMapper.INSTANCE.postToPostDto(createdPost);
    return ResponseEntity.ok(postDto);
  }

  @GetMapping("/{post_type_id}")
  public ResponseEntity<List<PostListDto>> getPostSummaries(@PathVariable("post_type_id") Long post_type_id) {
    List<PostListDto> postSummaries = postService.getPostSummaries(post_type_id);
    return ResponseEntity.ok(postSummaries);
  }

  @GetMapping("/{post_type_id}/postDetail/{postId}")
  public ResponseEntity<PostDto> getPost(@PathVariable("post_type_id") Long post_type_id, @PathVariable("postId") Long postId) {
    PostDto postDto = postService.getPostByPostTypeIdAndPostId(post_type_id, postId);
    return ResponseEntity.ok(postDto);
  }

  // 공감 저장하는 곳
  @PostMapping("/{postId}/like")
  public boolean likePost(@PathVariable("postId") Long postId, @RequestParam("userId") String userId){
    return postService.saveLike(postId, userId);
  }

  @PutMapping("{post_type_id}/post/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId, @RequestBody PostDto postDto){
    return postService.updatePost(postTypeId, postId, postDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{post_type_id}/post/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId){
    postService.deletePosts(postTypeId, postId);
    return ResponseEntity.noContent().build();
  }
}