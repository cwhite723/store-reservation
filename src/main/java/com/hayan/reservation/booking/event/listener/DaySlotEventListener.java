package com.hayan.reservation.booking.event.listener;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.event.DaySlotExpireEvent;
import com.hayan.reservation.booking.scheduler.SlotStatusScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DaySlotEventListener {

    private final SlotStatusScheduler slotStatusScheduler;

    @EventListener
    public void handleDaySlotExpireEvent(DaySlotExpireEvent event) {
        DaySlot daySlot = event.daySlot();
        slotStatusScheduler.scheduleDaySlotStatusUpdate(daySlot);
    }
}
