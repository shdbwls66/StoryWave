package com.example.storywave.Dto;

import com.example.storywave.Entity.User;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link User}
 */
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {
    String user_id;
    String username;
    String password;
    String nickname;
    String email;
    String role;
    String active_status;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}