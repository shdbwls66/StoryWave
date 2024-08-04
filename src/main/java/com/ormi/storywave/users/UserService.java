package com.ormi.storywave.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUsers)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto usersDto) {
        User users = usersDto.toUsers();
        users.setCreatedAt(LocalDateTime.now());
        User savedUsers = userRepository.save(users);
        return UserDto.fromUsers(savedUsers);
    }

    public Optional<UserDto> getUserById(String userId) {
        return userRepository.findById(userId)
                .map(UserDto::fromUsers);
    }


    public Optional<UserDto> updateUser(String userId, UserDto updatedUsers) {
        return userRepository.findById(userId)
                .map(users -> {
                    users.setPassword(updatedUsers.getPassword());
                    users.setNickname(updatedUsers.getNickname());
                    users.setUpdatedAt(LocalDateTime.now());
                    return UserDto.fromUsers(userRepository.save(users));
                });
    }

    public boolean selectId(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public boolean selectNickname(String nickname) {
        System.out.println(nickname);
        return userRepository.existsByNickname(nickname);
    }

    public boolean deleteUser(String userId) {
        return userRepository.findById(userId)
                .map(u->{
                    userRepository.delete(u);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public UserDto addUser(UserRequest.JoinDto joinDto) {
        //default 값 부여
        String defaultRole = "user";
        boolean defaultStatus = true;

        // 유저 객체 저장
        User savedUser = userRepository.save(
                User.builder()
                        .userId(joinDto.getUserId())
                        .password(joinDto.getPassword())
                        .email(joinDto.getEmail())
                        .nickname(joinDto.getNickname())
                        .role(defaultRole)
                        .activeStatus(defaultStatus)
                        .createdAt(LocalDateTime.now())
                        .build());
        return UserDto.fromUsers(savedUser);
    }

    public String loginUser(UserRequest.LoginDto loginDto) {

        // 로그인 정보와 일치하는 객체 불러오기
        Optional<UserDto> foundUser = userRepository.findById(loginDto.getUserId())
                .map(UserDto::fromUsers);

        // 일치하는 사용자가 없는 경우
        if (foundUser.isEmpty()) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
        }

        // 해당 아이디에 비밀번호가 일치하지 않는 경우
        if(!foundUser.get().getPassword().equals(loginDto.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
        }

        // 정지 회원인 경우 추가 필요
       /* if (!foundUser.get().isActiveStatus()) {
            // 영구 정지, 일시 정지를 위한 테이블 필요

        }
*/
        return foundUser.get().getUserId();
        // 객체를 반환하는 게 나은가..?(보완필요)
    }



   /* public void updateUserStatus(String userid, String activeStatus, String reason) {
        User user = userRepository.findById(userid).orElse(null);
        if (user != null) {
            user.setActiveStatus(activeStatus);
            userRepository.save(user);
        }

    }*/

    public Optional<UserDto> updateUserStatus(String userId, boolean activeStatus) {
        return userRepository.findById(userId)
                .map(users -> {
                    users.setActiveStatus(activeStatus);
                    return UserDto.fromUsers(userRepository.save(users));
                });
    }


    //adminuser login
    public String adminUserLogin(UserRequest.AdminDto adminDto) {

        // 로그인 정보와 일치하는 객체 불러오기
        Optional<UserDto> foundUser = userRepository.findByRole(adminDto.getRole())
                .map(UserDto::fromUsers);
        // admin이 아닌경우
        if (foundUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserDto user = foundUser.get();
        if (!"admin".equals(user.getRole())) {
            throw new RuntimeException("User is not an admin");
        }
        return foundUser.get().getUserId();
    }



}