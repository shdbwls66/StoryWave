package com.ormi.storywave;

import com.ormi.storywave.users.UserDto;
import com.ormi.storywave.users.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;



@Controller
public class HomeController {
    @Autowired
    private UserService userService; // UserService를 주입받아야 합니다.

    @GetMapping("/home")
    public String home(Model model, HttpSession session){
        // 세션에서 userId를 가져옵니다.
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            // userId가 세션에 있는 경우, UserDto를 조회합니다.
            Optional<UserDto> user = userService.getUserById(userId);
            if (user.isPresent()) {
                model.addAttribute("isLoggedIn", true);

            } else {
                // UserDto가 null인 경우, 로그인 상태가 아님
                model.addAttribute("isLoggedIn", false);
            }
        } else {
            // userId가 세션에 없는 경우, 로그인 상태가 아님
            model.addAttribute("isLoggedIn", false);
        }
        return "home";
    }
}
