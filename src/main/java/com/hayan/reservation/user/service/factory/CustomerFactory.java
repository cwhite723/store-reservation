package com.hayan.reservation.user.service.factory;

import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.dto.request.SignUpRequest;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("customer")
public class CustomerFactory implements UserFactory{

    @Override
    public Customer createUser(SignUpRequest request, String encodedPassword) {
        Set<String> roles = new HashSet<>();
        roles.add("CUSTOMER");

        return Customer.builder()
                .name(request.name())
                .contact(request.contact())
                .username(request.username())
                .password(encodedPassword)
                .roles(roles)
                .build();
    }
}
