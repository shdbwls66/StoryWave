package com.ormi.storywave.posts;

import com.ormi.storywave.board.PostListDto;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/board")
public class PostController {
  private final PostService postService;
  private final UserRepository userRepository;

  @Autowired
  public PostController(PostService postService, UserRepository userRepository) {
    this.postService = postService;
    this.userRepository = userRepository;
  }

  // 테스트 용도로 생성했으니, url 바꿀려면 편하게 바꾸어주시면 됩니다.
  @GetMapping("/0/post") // 카테고리 설정을 따로 하지 않아서 그런지 공지사항 게시판만 들어가짐
  public String noticeBoard(Model model) {
    return "/board/Noticepostlist";
  }

  @GetMapping("/1/post") // 카테고리 설정을 따로 하지 않아서 그런지 공지사항 게시판만 들어가짐
  public String movieBoard(Model model) {
    List<PostListDto> postList = postService.getPostSummaries(1L);
    return "/board/Moviepostlist";
  }

  @GetMapping("/2/post") // 카테고리 설정을 따로 하지 않아서 그런지 공지사항 게시판만 들어가짐
  public String bookBoard(Model model) {
    return "/board/Bookpostlist";
  }

  // 게시물 상세화면 조회
  @GetMapping("/{post_type_id}/post/{postId}")
  public String postsDetail( @PathVariable("post_type_id") Long postTypeId,
                             @PathVariable("postId") Long postId, @RequestParam("userId") String userId, Model model) {
    System.out.println("postsDetail 실행");
    // 게시물
    PostDto posts =
            postService
                    .getPostByPostTypeIdAndPostId(postTypeId, postId);

    UserDto users =
            userRepository
                    .findByUserId(userId)
                    .map(UserDto::fromUsers)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

    String writer = userRepository.findByPosts_Id(postId)
            .map(UserDto::fromUsers)
            .map(UserDto::getNickname)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트의 작성자를 찾을 수 없습니다."));

    // 좋아요 기능 구현
    // user 정보가 있을 때만 작동 -> 로그인 기능 구현하면 로그인 여부에 따라 기능 작동하도록 수정하면 될 듯
    boolean like = false;
    if (users != null) {
      String user_id = users.getUserId();
      like = postService.findPostLike(postId, user_id);
    }

    model.addAttribute("like", like);

    // 권한 테스트를 위해 임시로 작성
    boolean isAdmin = users.getRole().equals("admin");

    model.addAttribute("writer", writer); // 글쓴이

    model.addAttribute("posts", posts); //게시물

    model.addAttribute("users", users); // 유저

    model.addAttribute("isAdmin", isAdmin); // 권한

    model.addAttribute("comments", posts.getComments().size()); // 댓글 개수


    return "board/posts_detail";
  }

}