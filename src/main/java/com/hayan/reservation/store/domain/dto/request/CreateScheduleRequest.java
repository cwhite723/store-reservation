package com.hayan.reservation.store.domain.dto.request;

import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.WeeklySchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

public record CreateScheduleRequest(DayOfWeek dayOfWeek,

                                    Optional<LocalTime> openingTime,

                                    Optional<LocalTime> closingTime,

                                    boolean isClosed) {

    public WeeklySchedule toEntity(Store store) {
        WeeklySchedule.WeeklyScheduleBuilder scheduleBuilder = WeeklySchedule.builder()
                .dayOfWeek(dayOfWeek)
                .isClosed(isClosed)
                .store(store);

        openingTime.ifPresent(scheduleBuilder::openingTime);
        closingTime.ifPresent(scheduleBuilder::closingTime);

        return scheduleBuilder.build();
    }
}
