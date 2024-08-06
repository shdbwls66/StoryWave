package com.ormi.storywave.posts;

import com.ormi.storywave.board.Category;
import com.ormi.storywave.board.CategoryDto;
import com.ormi.storywave.board.PostListDto;
import com.ormi.storywave.comment.CommentRepository;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserService;
import com.ormi.storywave.users.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import java.util.List;


@Controller
@RequestMapping("/board")
public class PostController {
  private final PostService postService;
  private final UserService userService;

  @Autowired
  public PostController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  // 게시물 상세화면 조회
  @GetMapping("/{post_type_id}/post/{postId}")
//  public String postsDetail(@PathVariable("post_type_id") Long postTypeId,
//                            @PathVariable("postId") Long postId, HttpSession session, Model model) {
  public String postsDetail(@PathVariable("post_type_id") Long postTypeId,
          @PathVariable("postId") Long postId, @RequestParam("userId") String userId, Model model) {
    System.out.println("postsDetail 실행");
//        String userId = (String) session.getAttribute("userId");

    if (userId != null){
      Optional<UserDto> user = userService.getUserById(userId);
      if (user.isPresent()) {
        model.addAttribute("login", true);
        model.addAttribute("userId", userId);

      } else {
        model.addAttribute("login", false);
      }
    } else {
      model.addAttribute("login", false);
    }

    // 게시물
    PostDto posts =
            postService
                    .getPostByPostTypeIdAndPostId(postTypeId, postId);

    UserDto users = userService.getUserById(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

    String writer = userService.getUserByPostId(postId)
            .map(UserDto::getNickname)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트의 작성자를 찾을 수 없습니다."));

    // 좋아요 기능 구현
    boolean like = false;
    if (users != null) {
      String user_id = users.getUserId();
      like = postService.findPostLike(postId, user_id);
    }

    boolean isAdmin = users.getRole().equals("admin");
    model.addAttribute("like", like);

    // 권한 테스트를 위해 임시로 작성
    String role = users.getRole();

    model.addAttribute("like", like); // 공감
    model.addAttribute("users", users); // 유저
    model.addAttribute("writer", writer); // 글쓴이
    model.addAttribute("posts", posts); //게시물
    model.addAttribute("isAdmin", isAdmin); // 권한

    model.addAttribute("users", users); // 유저

    model.addAttribute("role", role); // 권한

    model.addAttribute("comments", posts.getComments().size()); // 댓글 개수

    return "board/posts_detail";
  }

  // 게시물 수정 페이지 조회
  @GetMapping("/{post_type_id}/post/{postId}/edit")
//  public String updatePost(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId, HttpSession session, Model model){
  public String updatePost(@PathVariable("post_type_id") Long postTypeId,
                           @PathVariable("postId") Long postId,
                           @RequestParam("userId") String userId, Model model){
//    String userId = (String) session.getAttribute("userId");
    Post post = postService.getPostByPostTypeIdAndPostId(postTypeId, postId).toPost();
    List<ImageDto> imageList = post.getImages().stream().map(ImageDto::fromImage).toList();
    Set<Category> categories = new HashSet<>(post.getCategories());
    post.setCategories(categories);

    UserDto user =
        userService.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 찾을 수 없습니다."));

    model.addAttribute("post", post);
    model.addAttribute("postTypeId", postTypeId);
    model.addAttribute("image", imageList);
    model.addAttribute("userId", user.getUserId());

    if (userId != null) {
      // userId가 세션에 있는 경우, UserDto를 조회합니다.
      Optional<UserDto> users = userService.getUserById(userId);
      if (users.isPresent()) {
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("userId", userId); // userId를 모델에 추가

      } else {
        // UserDto가 null인 경우, 로그인 상태가 아님
        model.addAttribute("isLoggedIn", false);
      }
    } else {
      // userId가 세션에 없는 경우, 로그인 상태가 아님
      model.addAttribute("isLoggedIn", false);
    }
    return "/board/editPost";
  }

  @PostMapping("/{post_type_id}/post/{postId}/edit")
  public String update(@PathVariable("post_type_id") Long postTypeId,
                       @PathVariable("postId") Long postId,
                       @RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @RequestParam("categories") List<String> categories, // 예시로 String 리스트 사용
                       @RequestPart(value = "images", required = false) MultipartFile[] images,
                       @ModelAttribute Post updatedPost){
    Post post = postService.getPostByPostTypeIdAndPostId(postTypeId, postId).toPost();
    post.setTitle(updatedPost.getTitle());
    post.setContent(updatedPost.getContent());

    if (images != null) {
      // 이미지 업로드 및 처리를 여기서 수행
      for (MultipartFile image : images) {
        // 이미지 파일 처리 로직
      }
    }

    return "redirect:board/" + postTypeId + "/post/" + postId;
  }

}