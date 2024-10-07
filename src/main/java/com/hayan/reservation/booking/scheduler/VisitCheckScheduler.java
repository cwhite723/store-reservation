package com.hayan.reservation.booking.scheduler;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
@RequiredArgsConstructor
public class VisitCheckScheduler {

    private final TaskScheduler taskScheduler;
    private final BookingService bookingService;

    public void scheduleVisitCheck(Booking booking) {
        LocalDate bookingDate = booking.getTimeslot().getDaySlot().getDate();
        LocalTime bookingTime = booking.getTimeslot().getTime();
        LocalDateTime bookingDateTime = LocalDateTime.of(bookingDate, bookingTime);

        Instant triggerTime = bookingDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();

        taskScheduler.schedule(() -> bookingService.noShow(booking), triggerTime);
    }
}
