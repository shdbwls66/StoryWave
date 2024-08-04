package com.ormi.storywave;

import com.ormi.storywave.board.PostListDto;
import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<PostListDto> noticePosts = latestPosts(0L);
        List<PostListDto> moviePosts = latestPosts(1L);
        List<PostListDto> bookPosts = latestPosts(2L);

        model.addAttribute("noticePosts", noticePosts);
        model.addAttribute("moviePosts", moviePosts);
        model.addAttribute("bookPosts", bookPosts);

        return "home";
    }

    private List<PostListDto> latestPosts(Long post_type_id){
        List<PostListDto> posts = postService.getPostSummaries(post_type_id);
        posts.sort((p1, p2) -> p2.getUpdated_at().compareTo(p1.getUpdated_at()));

        // 최신 게시물 3개 추출
        return posts.stream().limit(3).toList();
    }

    @GetMapping("/home/login")
    public String afterLogin(Model model) {
        return "index_afterLogin";
    }
}
