package com.hayan.reservation.token;

import com.hayan.reservation.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public void storeRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(String.valueOf(userId), refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRATION_TIME(), TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(Long userId) {
        return redisTemplate.opsForValue().get(String.valueOf(userId));
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }
}