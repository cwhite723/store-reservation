package com.hayan.reservation.booking.domain.dto.response;

import com.hayan.reservation.booking.domain.DaySlot;

import java.util.List;
import java.util.stream.Collectors;

public record GetSlotResponse(DaySlotInfo daySlotInfo,
                              List<TimeSlotInfo> timeSlotInfoList) {

    public static GetSlotResponse of(DaySlot daySlot) {
        DaySlotInfo daySlotInfo = DaySlotInfo.of(daySlot);
        List<TimeSlotInfo> timeSlotInfoList = daySlot.getTimeSlots().stream()
                .map(TimeSlotInfo::of)
                .collect(Collectors.toList());

        return new GetSlotResponse(daySlotInfo, timeSlotInfoList);
    }
}
