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

    public List<UsersDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UsersDto::fromUsers)
                .collect(Collectors.toList());
    }

    public UsersDto createUser(UsersDto usersDto) {
        Users users = usersDto.toUsers();
        users.setCreated_at(LocalDateTime.now());
        Users savedUsers = userRepository.save(users);
        return UsersDto.fromUsers(savedUsers);
    }

    public Optional<UsersDto> getUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(UsersDto::fromUsers);
    }

    public Optional<UsersDto> updateUser(Integer userId, UsersDto updatedUsers) {
        return userRepository.findById(userId)
                .map(users -> {
                    users.setPassword(updatedUsers.getPassword());
                    users.setNickname(updatedUsers.getNickname());
                    users.setUpdated_at(LocalDateTime.now());
                    return UsersDto.fromUsers(userRepository.save(users));
                });
    }

    public boolean deleteUser(Integer userId) {
        return userRepository.findById(userId)
                .map(u->{
                    userRepository.delete(u);
                    return true;
                })
                .orElse(false);
    }
}
