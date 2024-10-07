package com.hayan.reservation.booking.scheduler;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

@Service
@RequiredArgsConstructor
public class SlotStatusScheduler {

    private final TaskScheduler taskScheduler;

    @Transactional
    public void scheduleTimeSlotDeadline(TimeSlot timeSlot, int deadlineHour) {
        LocalDate date = timeSlot.getDaySlot().getDate();
        LocalTime time = timeSlot.getTime();
        LocalDateTime deadlineTime = LocalDateTime.of(date, time).minusHours(deadlineHour);
        Instant triggerTime = deadlineTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();

        taskScheduler.schedule(timeSlot::afterDeadline, triggerTime);
    }

    @Transactional
    public void scheduleDaySlotStatusUpdate(DaySlot daySlot) {
        LocalDateTime midnight = daySlot.getDate().atStartOfDay().plusDays(1);
        Instant triggerTime = midnight.atZone(ZoneId.systemDefault()).toInstant();

        taskScheduler.schedule(daySlot::outOfDate, triggerTime);
    }

}
