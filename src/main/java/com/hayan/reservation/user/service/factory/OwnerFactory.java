package com.hayan.reservation.user.service.factory;

import com.hayan.reservation.user.domain.Owner;
import com.hayan.reservation.user.domain.dto.request.SignUpRequest;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("owner")
public class OwnerFactory implements UserFactory {

    @Override
    public Owner createUser(SignUpRequest request, String encodedPassword) {
        Set<String> roles = new HashSet<>();
        roles.add("OWNER");

        return Owner.builder()
                .name(request.name())
                .contact(request.contact())
                .username(request.username())
                .password(encodedPassword)
                .roles(roles)
                .build();
    }
}