package com.hayan.reservation.booking.domain.dto.response;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.domain.constant.DaySlotStatus;

import java.time.LocalDate;

public record DaySlotInfo(Long daySlotId,
                          LocalDate date,
                          DaySlotStatus daySlotStatus) {

    public static DaySlotInfo of(DaySlot daySlot) {
        return new DaySlotInfo(daySlot.getId(), daySlot.getDate(), daySlot.getDaySlotStatus());
    }
}
