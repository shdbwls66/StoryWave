package com.ormi.storywave.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ormi.storywave.users.UserDto.fromUsers;

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

    public Optional<UserDto> getUserByPostId(Long postId){
        return userRepository.findByPosts_Id(postId)
                .map(UserDto::fromUsers);
    }

    public Optional<UserDto> updateUser(String userId, UserDto updatedUsers) {
        return userRepository.findById(userId)
                .map(users -> {
                    users.setPassword(updatedUsers.getPassword());
                    users.setUsername(updatedUsers.getUsername());
                    users.setEmail(updatedUsers.getEmail());
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
        String defaultRole = "USER";
        boolean defaultStatus = true;

        // 유저 객체 저장
        User savedUser = userRepository.save(
                User.builder()
                        .userId(joinDto.getUserId())
                        .password(joinDto.getPassword())
                        .email(joinDto.getEmail())
                        .username(joinDto.getUsername())
                        .nickname(joinDto.getNickname())
                        .role(defaultRole)
                        .activeStatus(defaultStatus)
                        .createdAt(LocalDateTime.now())
                        .build());
        return UserDto.fromUsers(savedUser);
    }

    public UserDto loginUser(UserRequest.LoginDto loginDto) {

        // 로그인 정보와 일치하는 객체 불러오기
        Optional<UserDto> foundUser = userRepository.findById(loginDto.getUserId())
                .map(UserDto::fromUsers);

        // 일치하는 사용자가 없는 경우
        if (foundUser.isEmpty()) {
            return null;
        }

        // 해당 아이디에 비밀번호가 일치하지 않는 경우
        if(!foundUser.get().getPassword().equals(loginDto.getPassword())) {
            return null;
        }

        return foundUser.orElse(null);
    }



    public String getUserRole(String userId) {
        return findByUserId(userId).getRole();
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    public UserDto changeUserStatus(String userId, UserDto userDto) {
        User user = findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }


        // User 상태 업데이트
        user.setActiveStatus(userDto.isActiveStatus());
        user.setBanReason(userDto.getBanReason()); // UserDto에 추가된 banReason 설정
        user.setBanPeriod(userDto.getBanPeriod()); // UserDto에 추가된 banPeriod 설정

        // 변경 사항 저장
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user); // 사용자 정보 저장

        // UserDto로 변환하여 반환
        return fromUsers(user); // User 객체를 UserDto로 변환
    }




    /*private User iaAdmin(String userId) {
        return users.stream()
                .filter(p -> p.getId(id).equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 글을 찾을 수 없습니다."));
    }*/


}