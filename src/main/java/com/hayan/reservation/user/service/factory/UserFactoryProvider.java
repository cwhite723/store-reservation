package com.hayan.reservation.user.service.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserFactoryProvider {

    private final Map<String, UserFactory> userFactories;

    public UserFactory getFactory(String type) {
        UserFactory factory = userFactories.get(type.toLowerCase());

        return factory;
    }
}
