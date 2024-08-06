package com.ormi.storywave;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(Model model) {
//        model.addAttribute("message", "Hello World!");
        return "home";
    }

    // 홈화면에 띄울 게시물 데이터 입니다.
    List<PostListDto> noticePosts = latestPosts(0L);
    List<PostListDto> moviePosts = latestPosts(1L);
    List<PostListDto> bookPosts = latestPosts(2L);

    model.addAttribute("noticePosts", noticePosts);
    model.addAttribute("moviePosts", moviePosts);
    model.addAttribute("bookPosts", bookPosts);

    return "home"; // 반환할 뷰 이름
  }

  @GetMapping("/home/login")
//  public String afterLogin(Model model, HttpSession session) {
//    String userId = (String) session.getAttribute("userId");
  public String afterLogin(Model model, @RequestParam("userId") String userId) {
    if (userId != null) {
      Optional<UserDto> user = userService.getUserById(userId);
      if (user.isPresent()) {
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("userId", userId);
      } else {
        model.addAttribute("isLoggedIn", false);
      }
    } else {
      model.addAttribute("isLoggedIn", false);
    }

    List<PostListDto> noticePosts = latestPosts(0L);
    List<PostListDto> moviePosts = latestPosts(1L);
    List<PostListDto> bookPosts = latestPosts(2L);

    model.addAttribute("noticePosts", noticePosts);
    model.addAttribute("moviePosts", moviePosts);
    model.addAttribute("bookPosts", bookPosts);

    return "index_afterLogin";
  }


  // 최신 게시물 3개 추출
  private List<PostListDto> latestPosts(Long post_type_id) {
    List<PostListDto> posts = postService.getPostSummaries(post_type_id);
    posts.sort((p1, p2) -> p2.getUpdated_at().compareTo(p1.getUpdated_at()));

    return posts.stream().limit(3).toList();
  }
}
