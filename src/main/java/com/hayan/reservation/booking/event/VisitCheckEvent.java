package com.hayan.reservation.booking.event;

import com.hayan.reservation.booking.domain.Booking;

public record VisitCheckEvent(Booking booking) {
}
