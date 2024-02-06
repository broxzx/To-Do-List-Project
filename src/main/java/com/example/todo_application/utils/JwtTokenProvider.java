package com.example.todo_application.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtTokenProvider {


    @Value("${jwt.secretKey}")
    private String secretKey;


    @Value("${jwt.time}")
    private Duration expirationTime;

    public String generateJwtToken(UserDetails userDetails) {
        HashMap<String, Object> hashMap = new HashMap<>();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        hashMap.put("roles", roles);

        Date issuedTime = new Date();
        Date expiredTime = new Date(issuedTime.getTime() + expirationTime.toMillis());

        return Jwts
                .builder()
                .claims(hashMap)
                .subject(userDetails.getUsername())
                .issuedAt(issuedTime)
                .expiration(expiredTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getAllClaims(token)
                .getSubject();
    }

    public List<String> getAllRoles(String token) {
        return getAllClaims(token)
                .get("roles", List.class);
    }
}
