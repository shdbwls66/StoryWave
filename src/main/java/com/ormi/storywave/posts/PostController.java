package com.ormi.storywave.posts;

import com.ormi.storywave.comment.CommentDto;
import com.ormi.storywave.comment.CommentRepository;
import com.ormi.storywave.users.UserRepository;
import com.ormi.storywave.users.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class PostController {
  private final PostsService postsService;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;

  @Autowired
  public PostController(PostsService postsService, UserRepository userRepository, CommentRepository commentRepository) {
    this.postsService = postsService;
    this.userRepository = userRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping("/post/login")
  @ResponseBody
  public String login(Model model) {
    return "board/로그인 화면 파일 이름";
  }

  // 게시물 상세화면 조회
  @GetMapping("/post/{postId}")
  public String postsDetail(
      @PathVariable("postId") Integer postId, @RequestParam("userId") Integer userId, Model model) {
    System.out.println("postsDetail 실행");
    PostDto posts =
        postsService
            .getPostById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트는 존재하지 않습니다."));

    UsersDto users =
        userRepository
            .findById(userId)
            .map(UsersDto::fromUsers)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

    String writer = userRepository.findByPosts_Id(postId)
            .map(UsersDto::fromUsers)
            .map(UsersDto::getNickname)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트의 작성자를 찾을 수 없습니다."));

    String role = users.getRole();

    model.addAttribute("writer", writer); // 글쓴이

    model.addAttribute("posts", posts);

    model.addAttribute("users", users);

    model.addAttribute("role", role); // 권한

    model.addAttribute("comments", posts.getCommentsList().size()); // 댓글 개수

    return "board/posts_detail";
  }
}
