package com.hayan.reservation.user.service;

import com.hayan.reservation.user.domain.User;
import com.hayan.reservation.user.domain.dto.request.SignInRequest;
import com.hayan.reservation.user.domain.dto.request.SignUpRequest;
import org.springframework.security.core.Authentication;

public interface UserService {
    void register(SignUpRequest request);
    Authentication authenticate(SignInRequest request);
    User getByUsername(String username);
    User getById(Long id);
}
