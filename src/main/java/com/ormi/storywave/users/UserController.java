package com.ormi.storywave.users;

import jakarta.servlet.http.HttpSession;
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
        UserDto addedUser = userService.addUser(joinDto);
//        model.addAttribute("user", addedUser);
        return "join/welcome";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserRequest.LoginDto loginDto, HttpSession session, Model model) {

        UserDto loginResult = userService.loginUser(loginDto);

        if (loginResult == null) { // 로그인 실패
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
            return "login/login";
        } else if (!loginResult.isActiveStatus()) { // 정지회원의 경우
            return "login/ban";
        } else { // 로그인 성공
            session.setAttribute("userId", loginResult.getUserId());
//            return "redirect:/home";
            return "index_afterLogin";
//            return "test";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            session.invalidate();
        }
        return "redirect:/home";
    }
}
