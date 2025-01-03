package com.aditya.habittracker.config;

import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import com.aditya.habittracker.entity.User;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "dGN5dVN5ZG5jTTQ0bDdtYmYzM2l2cGZmbjhaVnJjdWVPUDA5NjlEViQw"; // Use a secure key in production
    SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());


    public String generateToken(User user) {
        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("name", user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class).toLowerCase();
    }

    public boolean validateToken(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

    }
}
