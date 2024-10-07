package com.hayan.reservation.user.service;

import com.hayan.reservation.token.TokenResponse;
import com.hayan.reservation.user.domain.dto.request.SignInRequest;
import com.hayan.reservation.user.domain.dto.response.SignInResponse;

import java.util.Optional;

public interface AuthenticationService {
    SignInResponse getUserInfo(SignInRequest request);
    TokenResponse authenticateAndGenerateToken(SignInRequest request);
    Optional<Long> getLoggedInUserId();
}
