package com.hayan.reservation.booking.event;

import com.hayan.reservation.booking.domain.SlotOption;

public record SlotCreateEvent(Long storeId, SlotOption slotOption) {
}