package com.ormi.storywave.posts;

import com.ormi.storywave.users.UserRepository;
import com.ormi.storywave.users.Users;
import com.ormi.storywave.users.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostsService(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // userId가 있어야지 포스트 생성 가능
    public PostDto createPosts(PostDto postDto, Integer userId) {
        Posts posts = postDto.toPost();
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        posts.setThumbs(0);
        posts.setCreated_at(LocalDateTime.now());
        Posts savedPosts = postRepository.save(posts);
        users.addPost(savedPosts);
        return PostDto.fromPost(savedPosts);
    }

    @Transactional(readOnly = true)
    public Optional<PostDto> getPostById(Integer id) {
        return postRepository.findById(id)
                .map(PostDto::fromPost);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostDto::fromPost)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByTitleContaining(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(PostDto::fromPost)
                .collect(Collectors.toList());
    }

    // 글쓴이만 포스트 수정 가능
    public Optional<PostDto> updatePost(Integer postId, PostDto updatePostDto, Integer userId) {
        return postRepository
        .findById(postId)
        .filter(posts -> posts.getUsers().getId().equals(userId))
        .map(
            post -> {
              post.setTitle(updatePostDto.getTitle());
              post.setContent(updatePostDto.getContent());
              post.setUpdated_at(LocalDateTime.now());
              return PostDto.fromPost(postRepository.save(post));
            });
    }

    // 글쓴이나, role이 admin인 사람만 포스트 삭제 가능
    public boolean deletePosts(Integer postId, Integer userId) {
    UsersDto users =
        userRepository.findById(userId)
                .map(UsersDto::fromUsers)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
    return postRepository
        .findById(postId)
        .filter(posts -> posts.getUsers().getId().equals(userId) || users.getRole().equals("admin"))
        .map(
            posts -> {
              postRepository.delete(posts);
              return true;
            })
        .orElse(false);
    }
}
