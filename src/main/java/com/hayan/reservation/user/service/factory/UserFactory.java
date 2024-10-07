package com.hayan.reservation.user.service.factory;

import com.hayan.reservation.user.domain.User;
import com.hayan.reservation.user.domain.dto.request.SignUpRequest;

public interface UserFactory {
    User createUser(SignUpRequest signUpRequest, String encodedPassword);
}
