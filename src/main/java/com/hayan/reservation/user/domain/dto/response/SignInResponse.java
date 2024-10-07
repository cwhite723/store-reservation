package com.hayan.reservation.user.domain.dto.response;

import lombok.Builder;

@Builder
public record SignInResponse(Long id,
                             String username,
                             String type) {
}
