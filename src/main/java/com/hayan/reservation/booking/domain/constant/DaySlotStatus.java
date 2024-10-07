package com.hayan.reservation.booking.domain.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DaySlotStatus {
    AVAILABLE("예약 가능"),
    CLOSED("예약 마감"),
    OUT_OF_DATE("날짜 지남"),
    STORE_CLOSED("매장 휴무");

    @JsonValue
    private final String description;
}
