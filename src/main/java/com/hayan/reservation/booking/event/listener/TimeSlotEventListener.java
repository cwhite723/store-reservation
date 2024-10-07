package com.hayan.reservation.booking.event.listener;

import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.event.TimeSlotDeadlineEvent;
import com.hayan.reservation.booking.scheduler.SlotStatusScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeSlotEventListener {

    private final SlotStatusScheduler slotStatusScheduler;

    @EventListener
    public void handleTimeSlotDeadlineEvent(TimeSlotDeadlineEvent event) {
        TimeSlot timeSlot = event.timeSlot();
        int deadlineHour = event.deadlineHour();
        slotStatusScheduler.scheduleTimeSlotDeadline(timeSlot, deadlineHour);
    }
}
