package com.ormi.storywave.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/mypage")
public class AdminController {

    @Autowired
    private UserService userService;

    private List<User> users = new ArrayList<>();

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserDto> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "mypage/userList2";
    }

    @PutMapping("/updateUserStatus/{id}")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @PathVariable("id") Long id,
            @RequestBody User updateUserStatus) {

        User user = users.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾지못하였습니다."));

        user.setActiveStatus(updateUserStatus.getActiveStatus());

        Map<String, String> response = new HashMap<>();
        response.put("success", "true");

        return ResponseEntity.ok(response);
    }

}
