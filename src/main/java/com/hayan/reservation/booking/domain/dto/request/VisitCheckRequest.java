package com.hayan.reservation.booking.domain.dto.request;

import jakarta.validation.constraints.Pattern;

import java.time.LocalTime;

public record VisitCheckRequest(LocalTime currentTime,

                                String name,

                                @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                                        message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
                                String contact) {
}
