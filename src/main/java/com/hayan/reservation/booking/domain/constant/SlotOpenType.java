package com.hayan.reservation.booking.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlotOpenType {
    DAILY("하루씩 예약 Open"),
    WEEKLY("매주 지정 요일 다음 주 예약 Open"),
    MONTHLY("매월 지정 날짜 다음 달 예약 Open");

    private final String description;
}
