package com.ormi.storywave.auth;

import com.ormi.storywave.users.User;
import com.ormi.storywave.users.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            return "redirect:/mypage";
        } else {
            return "redirect:/login?error";
        }
    }
}