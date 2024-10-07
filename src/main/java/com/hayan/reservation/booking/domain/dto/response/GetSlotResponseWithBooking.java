package com.hayan.reservation.booking.domain.dto.response;

import java.util.List;

public record GetSlotResponseWithBooking(DaySlotInfo daySlotInfo,
                                         List<TimeSlotInfoWithBooking> timeSlotInfoList) {
}