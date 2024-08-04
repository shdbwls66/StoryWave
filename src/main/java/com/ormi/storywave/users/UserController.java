package com.ormi.storywave.users;

import com.ormi.storywave.posts.Post;
import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/join")
    public String addUserForm(Model model) {
        return "join/join";
    }

    @PostMapping("/join")
    public String addUser(@ModelAttribute UserRequest.JoinDto joinDto) {
        UserDto addedUser = userService.addUser(joinDto);
//        model.addAttribute("user", addedUser);
        return "join/welcome";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserRequest.LoginDto loginDto, Model model) {

        try { // userId를 받는 게 나을까 객체를 받는 게 나을까
            String userId = userService.loginUser(loginDto);
        } catch (IllegalArgumentException e){
            model.addAttribute("message", e.getMessage());
            return "login/login";
        }

        return "home"; // index_afterLogin에 맵핑이 안 된 게 많아서 오류 발생. 추후 변경
    }




    //Admin User


    private List<User> users = new ArrayList<>();


    @GetMapping("/admin/userControl")
    public String getAllUsers(Model model){
        List<UserDto> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/userList";
    }





}
