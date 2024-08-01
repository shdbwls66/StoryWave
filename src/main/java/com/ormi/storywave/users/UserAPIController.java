package com.ormi.storywave.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {
    private final UserService userService;

    @Autowired
    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UsersDto>> getAllUsers() {
        List<UsersDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PostMapping
    public ResponseEntity<UsersDto> createUser(@RequestBody UsersDto usersDto) {
        UsersDto createdUsers = userService.createUser(usersDto);
        return new ResponseEntity<>(createdUsers, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable("userId") String userId) {
        return userService
                .getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{userId}")
    public ResponseEntity<UsersDto> updateUser(@PathVariable("userId") String userId, @RequestBody UsersDto usersDto) {
        return userService
                .updateUser(userId, usersDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        boolean deleted = userService.deleteUser(userId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/confirmId")
    @ResponseBody
    public ResponseEntity<Boolean> confirmId(String userId) {
        boolean result = true;

        if(userId.trim().isEmpty()) {
            result = false;
        } else {
            result = !userService.selectId(userId);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/confirmNickname")
    @ResponseBody
    public ResponseEntity<Boolean> confirmNickname(String nickname) {
        boolean result = true;
        if(nickname.trim().isEmpty()) {
            result = false;
        } else {
            result = !userService.selectNickname(nickname);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
