package com.hayan.reservation.user.service.impl;

import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.user.domain.User;
import com.hayan.reservation.user.domain.dto.request.SignInRequest;
import com.hayan.reservation.user.domain.dto.request.SignUpRequest;
import com.hayan.reservation.user.repository.UserRepository;
import com.hayan.reservation.user.service.UserService;
import com.hayan.reservation.user.service.factory.UserFactory;
import com.hayan.reservation.user.service.factory.UserFactoryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserFactoryProvider userFactoryProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public void register(SignUpRequest request) {
        UserFactory userFactory = userFactoryProvider.getFactory(request.type());
        if (userFactory == null) {
            throw new CustomException(ErrorCode.REQUEST_VALIDATION_FAIL, "올바르지 않은 회원 유형입니다.");
        }

        User user = userFactory.createUser(request, passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    @Override
    public Authentication authenticate(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        return authentication;
    }

    @Override
    public User getByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}