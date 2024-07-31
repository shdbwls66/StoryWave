package com.ormi.storywave.users;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/join")
    public String addUserForm(Model model) {
        return "join";
    }

    @PostMapping("/join")
    public String addUser(@ModelAttribute UserRequest.JoinDto joinDto) {
        UsersDto addedUser = userService.addUser(joinDto);
//        model.addAttribute("user", addedUser);
        return "welcome";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserRequest.LoginDto loginDto, Model model) {
        System.out.println(loginDto.getUserId());
        System.out.println(loginDto.getPassword());
        String userId = userService.loginUser(loginDto);
//        return "home/{postId}";
//        return "index_afterLogin"; // 해당 html에 맵핑이 안 된 게 많아서 에러 발생. 해결하면 이걸로 변경
        return "home";
    }
}
