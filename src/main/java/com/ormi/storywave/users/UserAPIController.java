package com.ormi.storywave.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {
    private final UserService userService;

    @Autowired
    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }*/


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /*@GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId, @RequestBody UserDto userDto) {
        return userService
                .getUserById(userId, userDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @RequestBody UserDto userDto) {

        if (!userId.equals(userDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userDto.getNickname() == null || userDto.getNickname().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return userService
                .updateUser(userId, userDto)
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
