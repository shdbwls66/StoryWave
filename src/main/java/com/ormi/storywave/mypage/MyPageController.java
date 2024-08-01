package com.ormi.storywave.mypage;

import com.ormi.storywave.comment.CommentService;
import com.ormi.storywave.posts.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final CommentService commentService;
    private final PostsService postsService;

    @Autowired
    public MyPageController(CommentService commentService, PostsService postsService) {
        this.commentService = commentService;
        this.postsService = postsService;
    }

    // 마이페이지 컨트롤러 안에 내 댓글, 내 게시물 기능 넣을지? 아님 각 컨트롤러에 넣을지?

    @GetMapping
    public String showMyPage(Model model) {
        return "mypage/mypage";
    }

    @GetMapping("/quit")
    public String showQuitPage(Model model) {
        return "mypage/quit";
    }


}
