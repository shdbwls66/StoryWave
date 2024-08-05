package com.ormi.storywave.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {
    @Getter
    @AllArgsConstructor
    public static class JoinDto {
        private String userId;
        private String password;
        private String passwordConfirm;
        private String username;
        private String email;
        private String nickname;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDto{
        private String userId;
        private String password;
    }
}
