package com.ormi.storywave;

import com.ormi.storywave.users.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
//        model.addAttribute("message", "Hello World!");
        return "home";
    }

    //admin일경우 adminMypage로 감.. id수정 필요
    @GetMapping("/mypage")
    public String mypage(@RequestParam("Id") String userId, Model model) {
        User foundUser = UserService.getUserById(userId);

        if (UserService.isAdmin(foundUser)) {
            return "mypage/adminMypage";
        } else {
            return "mypage/mypage";
        }
    }
}
