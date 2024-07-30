package com.ormi.storywave.comment;

import org.springframework.beans.factory.annotation.Autowired;
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

}
