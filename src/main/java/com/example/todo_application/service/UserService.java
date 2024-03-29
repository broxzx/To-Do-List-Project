package com.example.todo_application.service;

import com.example.todo_application.entity.UserEntity;
import com.example.todo_application.exception.UserNotFoundException;
import com.example.todo_application.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserEntity saveUser(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserEntity findByUsername(String username) {
        Optional<UserEntity> foundUserEntity = userRepository.findByUsername(username);

        log.error("User with username %s was not found".formatted(username));

        return foundUserEntity.orElseThrow(
                () -> new UserNotFoundException("User with username %s was not found".formatted(username))
        );
    }
}
