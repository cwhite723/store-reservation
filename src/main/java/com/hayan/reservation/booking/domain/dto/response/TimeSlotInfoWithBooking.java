package com.hayan.reservation.booking.domain.dto.response;

import com.hayan.reservation.booking.domain.constant.TimeSlotStatus;

import java.time.LocalTime;
import java.util.List;

public record TimeSlotInfoWithBooking(Long id,
                           LocalTime time,
                           TimeSlotStatus timeSlotStatus,
                           List<BookingInfo> bookingList) {
}