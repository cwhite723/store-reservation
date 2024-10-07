package com.hayan.reservation.booking.event;

import com.hayan.reservation.booking.domain.TimeSlot;

public record TimeSlotDeadlineEvent(TimeSlot timeSlot, int deadlineHour) {
}
