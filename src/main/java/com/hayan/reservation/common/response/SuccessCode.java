package com.hayan.reservation.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements ResponseCode {
    SUCCESS(HttpStatus.OK, "00", "success");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
