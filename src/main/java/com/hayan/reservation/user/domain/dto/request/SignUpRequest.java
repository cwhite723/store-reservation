package com.hayan.reservation.user.domain.dto.request;

public record SignUpRequest(String type,
                            String name,
                            String contact,
                            String username,
                            String password) {
}
