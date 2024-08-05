package com.ormi.storywave;

import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.ormi.storywave.board.PostListDto;
import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.PostService;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@Controller
public class HomeController {
  @Autowired private PostService postService;
  @Autowired private UserService userService; // UserService를 주입받아야 합니다.

  @GetMapping("/home")
  public String home(Model model, HttpSession session) {
    // 세션에서 userId를 가져옵니다.
    String userId = (String) session.getAttribute("userId");

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
  public String afterLogin(Model model, HttpSession session) {
    String userId = (String) session.getAttribute("userId");

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
