package com.hayan.reservation.booking.domain.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSlotStatus {
    AVAILABLE("예약 가능"),
    AFTER_DEADLINE("예약 기간 지남"),
    CLOSED("예약 마감");

    @JsonValue
    private final String description;
}
