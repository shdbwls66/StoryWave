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
  private final HttpSession session;

  @Autowired
  public PostController(PostService postService, UserRepository userRepository, HttpSession httpSession) {
    this.postService = postService;
    this.userRepository = userRepository;
    this.session = httpSession;
  }


  // 게시판 링크는 테스트 용도로 생성했으니, 수정 하려면 편하게 수정 하시면 됩니다.
//  @GetMapping("/0/post")
//  public String noticeBoard(Model model) {
//    return "/board/Noticepostlist";
//  }

  @GetMapping("/1/post")
  public String movieBoard(Model model) {
    List<PostListDto> postList = postService.getPostSummaries(1L);
//    arr.add("comedy");
//    arr.add("crime");
//    arr.add("horror");
//    arr.add("romance");
//    arr.add("animation");
//    arr.add("documentary");
//    arr.add("sci-fi-fantasy");
    model.addAttribute("categoryFilter", postList); // 임시 작성
    return "/board/Moviepostlist";
  }

//  @GetMapping("/2/post")
//  public String bookBoard(Model model) {
//    List<PostListDto> test = postService.getPostSummaries(2L);
//    model.addAttribute("categoryFilter", test); // 임시 작성
//    return "/board/Bookpostlist";
//  }

  @GetMapping("{post_type_id}/new/post/")
  public String newPost(@PathVariable("post_type_id") Long postTypeId, Model model){

    if(postTypeId == 0L){
      return "board/Noticepostwrite";
    } else if(postTypeId == 1L){
      return "board/Moviepostwrite";
    } else if(postTypeId == 2L){
      return "board/Bookpostwrite";
    }

    return "index_afterLogin";
  }


  // 게시물 상세화면 조회
  @GetMapping("/{post_type_id}/post/{postId}")
  public String postsDetail( @PathVariable("post_type_id") Long postTypeId,
                             @PathVariable("postId") Long postId, Model model) {
    System.out.println("postsDetail 실행");

    // 게시물
    PostDto posts =
            postService
                    .getPostByPostTypeIdAndPostId(postTypeId, postId);

    String userId = (String) session.getAttribute("userId");
    if (userId == null) {
      throw new IllegalArgumentException("세션에서 사용자 ID를 찾을 수 없습니다.");
    }

    // 사용자 정보를 조회하기
    UserDto user = userRepository.findByUserId(userId)
            .map(UserDto::fromUsers)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

    // 비즈니스 로직 처리

    String writer = userRepository.findByPosts_Id(postId)
            .map(UserDto::fromUsers)
            .map(UserDto::getNickname)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트의 작성자를 찾을 수 없습니다."));

    // 좋아요 기능 구현
    // user 정보가 있을 때만 작동 -> 로그인 기능 구현하면 로그인 여부에 따라 기능 작동하도록 수정하면 될 듯
    boolean like = false;
    if (user != null) {
//      String user_id = user.getUserId();
      like = postService.findPostLike(postId, userId);
    }

    model.addAttribute("like", like);

    // 권한 테스트를 위해 임시로 작성
    boolean isAdmin = user.getRole().equals("admin");

    model.addAttribute("writer", writer); // 글쓴이

    model.addAttribute("posts", posts); //게시물

    model.addAttribute("isAdmin", isAdmin); // 권한

    model.addAttribute("comments", posts.getComments().size()); // 댓글 개수


    return "board/posts_detail";
  }

  // 게시물 삭제
  @PostMapping("/{post_type_id}/post/{postId}")
  public String deletePost(@PathVariable("post_type_id") Long postTypeId, @PathVariable("postId") Long postId){
    List<PostListDto> posts = postService.getPostSummaries(postTypeId);
    posts.removeIf(post-> post.getId().equals(postId));
    return "redirect:/board/" + postTypeId + "/post";
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