package com.hayan.reservation.booking.domain.dto.response;

import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.domain.constant.TimeSlotStatus;

import java.time.LocalTime;

public record TimeSlotInfo(Long id,
                           LocalTime time,
                           TimeSlotStatus timeSlotStatus) {

    public static TimeSlotInfo of(TimeSlot timeSlot) {
        return new TimeSlotInfo(timeSlot.getId(), timeSlot.getTime(), timeSlot.getTimeSlotStatus());
    }
}
