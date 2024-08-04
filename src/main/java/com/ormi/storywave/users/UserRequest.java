package com.ormi.storywave.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequest {
    @Getter
    @AllArgsConstructor
    public static class JoinDto {
        private String userId;
        private String password;
        private String passwordConfirm;
        private String email;
        private String nickname;

    }

    @Getter
    @AllArgsConstructor
    public static class LoginDto{
        private String userId;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class AdminDto{
        private String role;
    }
}
