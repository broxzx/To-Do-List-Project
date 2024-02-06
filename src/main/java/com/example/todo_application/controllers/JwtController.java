package com.example.todo_application.controllers;

import com.example.todo_application.dto.JwtRequestDto;
import com.example.todo_application.dto.UserDto;
import com.example.todo_application.entity.UserEntity;
import com.example.todo_application.security.UserDetailsServiceImpl;
import com.example.todo_application.service.UserService;
import com.example.todo_application.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final AuthenticationManager authentication;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/jwt/auth")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequestDto jwtRequestDto) {
        try {
            authentication.authenticate(new UsernamePasswordAuthenticationToken(jwtRequestDto.getUsername(), jwtRequestDto.getPassword(), null));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user cannot be authorized");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequestDto.getUsername());
        String token = jwtTokenProvider.generateJwtToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body("token: %s".formatted(token));
    }

    @PostMapping("/jwt/registration")
    public ResponseEntity<?> registration(@RequestBody UserDto userDto) {
        UserEntity user = userService.saveUser(
                new UserEntity(
                        null,
                        userDto.getUsername(),
                        userDto.getPassword(),
                        userDto.getEmail(),
                        null,
                        null,
                        null
                )
        );

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
