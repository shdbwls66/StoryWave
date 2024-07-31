package com.ormi.storywave.posts;

import com.ormi.storywave.comment.CommentDto;
import com.ormi.storywave.comment.CommentRepository;
import com.ormi.storywave.users.PostService;
import com.ormi.storywave.users.UserRepository;
import com.ormi.storywave.users.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class PostController {
  private final PostService postService;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;

  @Autowired
  public PostController(PostService postService, UserRepository userRepository, CommentRepository commentRepository) {
    this.postService = postService;
    this.userRepository = userRepository;
    this.commentRepository = commentRepository;
  }

  // 게시물 상세화면 조회
  @GetMapping("/post/{postId}")
  public String postsDetail(
          @PathVariable("postId") Long postId, @RequestParam("userId") String userId, Model model) {
    System.out.println("postsDetail 실행");
    PostDto posts =
            postService
                    .getPostById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 포스트는 존재하지 않습니다."));

    UsersDto users =
            userRepository
                    .findById(userId)
                    .map(UsersDto::fromUsers)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

    String writer = userRepository.findByPosts_PostId(postId)
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

  @GetMapping("/post/{postId}/comments/{commentId}")
  public String commentsControl(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @RequestParam("userId") String userId, Model model){
    CommentDto comment = commentRepository
            .findById(commentId)
            .map(CommentDto::fromComment)
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));

    // 댓글 단 유저
    String commentWriter = comment.getUserId();
    model.addAttribute("commentWriter", commentWriter);
    model.addAttribute("comment", comment);

    return "board/posts_detail";
  }
}
