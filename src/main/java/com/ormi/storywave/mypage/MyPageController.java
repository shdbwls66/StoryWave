package com.ormi.storywave.mypage;

import com.ormi.storywave.comment.CommentService;
import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostService;
import com.ormi.storywave.users.User;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/myPage")
public class MyPageController {

  private List<User> users = new ArrayList<>();
  private final CommentService commentService;
  private final PostService postService;
  private UserService userService;

  @Autowired
  public MyPageController(CommentService commentService, PostService postsService) {
    this.commentService = commentService;
    this.postService = postsService;
  }

  // 마이페이지 컨트롤러 안에 내 댓글, 내 게시물 기능 넣을지? 아님 각 컨트롤러에 넣을지?



  @GetMapping
  public String showMypage(HttpSession session) {

    //사용자 정보 확인 가능한지 해봐야함 redirection은 잘 됨
    User user = (User) session.getAttribute("user");

    // 사용자 정보가 없으면 로그인 페이지로 리디렉션
    if (user == null) {
      return "redirect:/login";
    }


    String userId = user.getUserId();
    String role = userService.getUserRole(userId);


    if ("ADMIN".equals(role)) {
      return "redirect:/adminMypage";
    } else if ("USER".equals(role)) {
      return "redirect:/userMyPage";
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
    return "home"; // mypage/mypost.html 템플릿을 반환
  }


 /* //admin mypage중 suercontrol
  @GetMapping("/admin/userControl")
  public String getAllUsers(Model model){
    List<UserDto> users = UserService.getAllUsers();
    model.addAttribute("users", users);
    return "admin/userList";
  }*/
}
