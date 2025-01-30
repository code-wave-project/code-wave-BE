package com.example.member.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000; // 유효시간: 1시간
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 유효기간: 7일

    private final Key key;

    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 비밀번호 재설정 jwt토큰 생성 (15분 만료)
    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // access토큰 생성 (1시간 만료)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)) // 1시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh 토큰 생성 (7일 만료)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)) // 7일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // jwt토큰 검증
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // 토큰의 이메일 반환
        } catch (JwtException | IllegalArgumentException e) {
            return null; // 유효하지 않은 토큰
        }
    }

    // 토큰이 만료가 되었는지 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true; // 토큰이 유효하지 않은 경우 만료된 것으로 간주
        }
    }
}
