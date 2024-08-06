package com.ormi.storywave.users;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    private List<User> users = new ArrayList<>();



    @GetMapping("/userList")
    public String getAllUsers(HttpSession httpSession, Model model) {

        String findUserId = (String)httpSession.getAttribute("userId");

        UserDto userDto = userService.getUserById(findUserId).orElse(null);

        // 사용자 정보가 없으면 로그인 페이지로 리디렉션
        if (userDto == null) {
            return "redirect:/login";
        }

        List<UserDto> users = userService.getAllUsers();
        model.addAttribute("users", users);

        String role = userService.getUserRole(userDto.getUserId());

        if ("ADMIN".equals(role)){
            return "mypage/userList";
        } else if ("USER".equals(role)) {
            return "donotaccess";
        } else {
            return "redirect:/error";
        }

    }

    @GetMapping("/adminMypage")
    public String showAdminMyPage() {
        return "mypage/adminMypage"; // 관리자 마이페이지 뷰
    }

 /*   @PutMapping("/updateUserStatus/{userId}")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @PathVariable("userId") String userId,
            @RequestBody User updateUserStatus) {

        User user = users.stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾지못하였습니다."));

        user.setActiveStatus(updateUserStatus.isActiveStatus());

        Map<String, String> response = new HashMap<>();
        response.put("success", "true");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mypage/{userId}")
    @ResponseBody
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String userId) {
        User user = userService.findById(userId);
        if (user != null) {
            UserDto userDto = UserDto.fromUsers(user);
            return ResponseEntity.ok(userDto);
        } else {
            // 적절한 오류 처리
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/mypage/{userId}")
    @ResponseBody
    public UserDto getUserInfo(@PathVariable String userId) {
        User user = userService.findByUserId(userId);
        if (user != null) {
            return UserDto.fromUsers(user);
        } else {
            return new UserDto();
        }

    }

    @PostMapping("/mypage/{userId}/reject")
    public UserDto updateUserStatus(@PathVariable String userId,
                                    @RequestBody UserDto userDto) {
        // User 상태 변경
        return userService.changeUserStatus(userId, userDto);
    }



}
