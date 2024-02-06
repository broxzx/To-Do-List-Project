package com.example.todo_application.security;

import com.example.todo_application.entity.UserEntity;
import com.example.todo_application.exception.UserNotFoundException;
import com.example.todo_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity findUserEntity =  userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("user with username: %s not found".formatted(username))
                );

        return UserDetailsImpl.build(findUserEntity);
    }
}
