package com.ormi.storywave.posts;

import com.ormi.storywave.board.PostListDto;
import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserRepository;
import jakarta.servlet.http.HttpSession;
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
  @GetMapping("{post_type_id}/new/post/")
  public String newPost(@PathVariable("post_type_id") Long postTypeId, Model model){
    return "index_afterLogin";
  }


  // 게시물 상세화면 조회
  @GetMapping("/{post_type_id}/post/{postId}")
  public String postsDetail(@PathVariable("post_type_id") Long postTypeId,
                            @PathVariable("postId") Long postId, HttpSession session, @RequestParam("userId") String userId, Model model) {
    System.out.println("postsDetail 실행");

    // 게시물
    PostDto posts =
            postService
                    .getPostByPostTypeIdAndPostId(postTypeId, postId);
//    UserDto users = (UserDto) session.getAttribute("users");

//    String userId = (String) session.getAttribute("qwer123");

    UserDto users = userRepository.findByUserId(userId)
            .map(UserDto::fromUsers)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

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

  // 게시물 삭제
  @PostMapping("/{post_type_id}/post/{postId}")
  public String deletePost(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId){
    List<PostListDto> posts = postService.getPostSummaries(postTypeId);
    posts.removeIf(post-> post.getId().equals(postId));
    if (postTypeId == 0L) {
      return "redirect:/board/Noticepostlist";
    } else if(postTypeId ==1L){
      return "redirect:/board/Moviepostlist";
    } else if(postTypeId == 2L){
      return "redirect:/board/Bookpostlist";
    }
    return "index_afterLogin";
  }

  // 게시물 수정
  @GetMapping("/{post_type_id}/post/{postId}/edit")
  public String updatePost(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId, Model model){
    Post post = postService.getPostByPostTypeIdAndPostId(postTypeId, postId).toPost();
    model.addAttribute("post", post);
    return "/board/editPost";
  }

  @PostMapping("/{post_type_id}/post/{postId}/edit")
  public String update(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId, @ModelAttribute Post updatedPost){
    Post post = postService.getPostByPostTypeIdAndPostId(postTypeId, postId).toPost();
    post.setTitle(updatedPost.getTitle());
    post.setContent(updatedPost.getContent());
    post.setImages(updatedPost.getImages());
    post.setCategories(updatedPost.getCategories());

    return "redirect:board/posts_detail";
  }

}