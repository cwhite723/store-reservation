package com.hayan.reservation.user.controller;

import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.jwt.JwtProperties;
import com.hayan.reservation.token.TokenResponse;
import com.hayan.reservation.user.domain.dto.request.SignInRequest;
import com.hayan.reservation.user.domain.dto.request.SignUpRequest;
import com.hayan.reservation.user.domain.dto.response.SignInResponse;
import com.hayan.reservation.user.service.AuthenticationService;
import com.hayan.reservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signup")
    public ApplicationResponse<Void> register(@RequestBody SignUpRequest request) {
        userService.register(request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @PostMapping("/signin")
    public ResponseEntity login(@RequestBody SignInRequest request) {
        TokenResponse tokens = authenticationService.authenticateAndGenerateToken(request);
        SignInResponse loginResponse = authenticationService.getUserInfo(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokens.accessToken());

        String refreshToken = tokens.refreshToken();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .headers(headers)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApplicationResponse.ok(loginResponse, SuccessCode.SUCCESS));
    }
}
