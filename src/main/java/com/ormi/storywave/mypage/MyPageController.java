package com.ormi.storywave.mypage;

import com.ormi.storywave.comment.CommentService;
import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostService;
import com.ormi.storywave.users.User;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

  private final CommentService commentService;
  private final PostService postService;
  private final UserService userService;

  @Autowired
  public MyPageController(CommentService commentService, PostService postsService, UserService userService) {
    this.commentService = commentService;
    this.postService = postsService;
    this.userService = userService;
  }

  // 마이페이지 컨트롤러 안에 내 댓글, 내 게시물 기능 넣을지? 아님 각 컨트롤러에 넣을지?

  @GetMapping
  public String showMyPage(HttpSession httpSession, Model model) {

    //사용자  로그인정보 확인 가능한지 해봐야함
    User user = (User) httpSession.getAttribute("user");

    // 사용자 정보가 없으면 로그인 페이지로
    if (user == null) {
      return "redirect:/login";
    }

    String role = userService.getUserRole(user.getUserId());

    if ("ADMIN".equals(role)){
      return "mypage/adminMypage";
    } else if ("USER".equals(role)) {
      return "mypage/mypage";
    } else {
      return "redirect:/error";
    }
  }

  @GetMapping("/quit")
  public String showQuitPage(Model model) {
    return "mypage/quit";
  }

  @GetMapping("/mypost")
  public String getAllPosts(
      Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
    int pageSize = 10; // 한 페이지에 보여줄 게시글 수
    Page<Post> postPage = postService.findPaginated(page, pageSize);

    // 모델에 데이터를 추가하여 뷰에 전달
    model.addAttribute("posts", postPage.getContent()); // 현재 페이지 게시물 리스트
    model.addAttribute("currentPage", page); // 현재 페이지 번호
    model.addAttribute("totalPages", postPage.getTotalPages()); // 총 페이지 수
    return "mypage/mypost"; // mypage/mypost.html 템플릿을 반환
  }
}
