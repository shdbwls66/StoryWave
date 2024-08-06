package com.ormi.storywave.mypage;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userMyPage")
public class UserMyPageController {

    @GetMapping
    public String showAdminMyPage() {
        return "mypage/mypage";
    }
}