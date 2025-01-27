package com.example.member.service;

import com.example.member.entity.RefreshToken;
import com.example.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String email, String token) {
        refreshTokenRepository.deleteByEmail(email); // 기존 토큰 제거
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(email);
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<String> getRefreshToken(String email) {
        return refreshTokenRepository.findByEmail(email).map(RefreshToken::getToken);
    }

    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}
