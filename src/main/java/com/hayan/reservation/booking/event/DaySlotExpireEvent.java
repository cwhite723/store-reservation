package com.hayan.reservation.booking.event;

import com.hayan.reservation.booking.domain.DaySlot;

public record DaySlotExpireEvent(DaySlot daySlot) {
}
