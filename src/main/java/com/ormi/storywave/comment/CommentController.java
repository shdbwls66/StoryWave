package com.ormi.storywave.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/myPage/myComment")
    public String getAllComments(
            Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10;
        Page<Comment> commentPage = commentService.findPaginated(page, pageSize);

        model.addAttribute("comments", commentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commentPage.getTotalPages());
        return "myPage/mycomment";
    }

//    @PostMapping("/comments")
//    @ResponseBody
//    public CommentDto createComment(@RequestBody CommentDto commentDto, @PathVariable("postId") Integer postId, @PathVariable("userId") String userId) {
//
//        return commentService.createComment(comment);
//    }

}
