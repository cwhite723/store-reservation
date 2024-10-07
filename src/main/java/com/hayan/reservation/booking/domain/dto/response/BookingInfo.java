package com.hayan.reservation.booking.domain.dto.response;

import com.hayan.reservation.booking.domain.constant.BookingStatus;
import com.querydsl.core.annotations.QueryProjection;

public record BookingInfo(Long bookingId,
                          String customerName,
                          String customerContact,
                          Integer guestCount,
                          String requestMessage,
                          BookingStatus status) {

    @QueryProjection
    public BookingInfo {
    }
}
