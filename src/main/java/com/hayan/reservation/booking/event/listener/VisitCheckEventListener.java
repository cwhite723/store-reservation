package com.hayan.reservation.booking.event.listener;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.event.VisitCheckEvent;
import com.hayan.reservation.booking.scheduler.VisitCheckScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitCheckEventListener {

    private final VisitCheckScheduler visitCheckScheduler;

    @EventListener
    public void handleVisitCheckEvent(VisitCheckEvent event) {
        Booking booking = event.booking();
        visitCheckScheduler.scheduleVisitCheck(booking);
    }
}
