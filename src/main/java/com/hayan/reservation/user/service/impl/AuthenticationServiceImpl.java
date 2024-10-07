package com.hayan.reservation.user.service.impl;

import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.jwt.JwtTokenProvider;
import com.hayan.reservation.security.model.CustomUserDetails;
import com.hayan.reservation.token.RefreshTokenService;
import com.hayan.reservation.token.TokenResponse;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;
import com.hayan.reservation.user.domain.User;
import com.hayan.reservation.user.domain.dto.request.SignInRequest;
import com.hayan.reservation.user.domain.dto.response.SignInResponse;
import com.hayan.reservation.user.service.AuthenticationService;
import com.hayan.reservation.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Override
    public SignInResponse getUserInfo(SignInRequest request) {
        User user = userService.getByUsername(request.username());
        String type = getUserType(user);

        return new SignInResponse(user.getId(), user.getUsername(), type);
    }

    @Override
    public TokenResponse authenticateAndGenerateToken(SignInRequest request) {
        Authentication authentication = userService.authenticate(request);

        return generateToken(authentication);
    }

    private TokenResponse generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());

        String accessToken = tokenProvider.generateAccessToken(userId, authentication.getName(), roles);
        String refreshToken = generateRefreshToken(userId);

        return new TokenResponse(accessToken, refreshToken);
    }

    private String generateRefreshToken(Long userId) {
        String refreshToken = tokenProvider.generateRefreshToken();
        refreshTokenService.storeRefreshToken(userId, refreshToken);

        return refreshToken;
    }

    @Override
    public Optional<Long> getLoggedInUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = tokenProvider.getTokenFromRequest(request);

        if (token == null || token.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(tokenProvider.getUserIdFromToken(token));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private String getUserType(User user) {
        if (user instanceof Owner) {
            return "Owner";
        } else if (user instanceof Customer) {
            return "Customer";
        } else {
            throw new CustomException(ErrorCode.INVALID_USER_TYPE);
        }
    }
}
