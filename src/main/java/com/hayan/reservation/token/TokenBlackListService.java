package com.hayan.reservation.token;

import com.hayan.reservation.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public void blacklistAccessToken(String accessToken) {
        Long expiration = jwtTokenProvider.getAccessTokenRemainingExpiration(accessToken);
        if (expiration > 0) {
            redisTemplate.opsForValue().set("blacklist:" + accessToken, "true", expiration, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }
}

