package com.ormi.storywave.users;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/userControl")
public class AdminUserController {

    private UserService userService;

    private List<User> users = new ArrayList<>();
    //Dto 를 배열로 추가해야하는가?

    @GetMapping
    public String getAllUsers(Model model){
        List<UserDto> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "admin/userList";
    }

    /*@PutMapping("/updateUserStatus/{id}")
    public ResponseEntity<Map<String, String>> updateUserStatus(@PathVariable("userId") String userId, @RequestBody User updateUserStatus) {
        users.setActiveStatus(updateUserStatus.getActiveStatus());

        Map<String, String> response = new HashMap<>();
        response.put("success", "true");

        return ResponseEntity.ok(response);
    }*/
}
