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
        return "join/join";
    }

    @PostMapping("/join")
    public String addUser(@ModelAttribute UserRequest.JoinDto joinDto) {
        UsersDto addedUser = userService.addUser(joinDto);
//        model.addAttribute("user", addedUser);
        return "join/welcome";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserRequest.LoginDto loginDto, Model model) {
<<<<<<< HEAD
        System.out.println(loginDto.getUserId());
        System.out.println(loginDto.getPassword());
        String userId = userService.loginUser(loginDto);
//        return "home/{postId}";
//        return "index_afterLogin"; // 해당 html에 맵핑이 안 된 게 많아서 에러 발생. 해결하면 이걸로 변경
        return "home";
=======

        try { // userId를 받는 게 나을까 객체를 받는 게 나을까
            String userId = userService.loginUser(loginDto);
        } catch (IllegalArgumentException e){
            model.addAttribute("message", e.getMessage());
            return "login/login";
        }

        return "home"; // index_afterLogin에 맵핑이 안 된 게 많아서 오류 발생. 추후 변경
>>>>>>> 9b21ba978f585fe6066f7b625d058dd18c18ef5b
    }
}
