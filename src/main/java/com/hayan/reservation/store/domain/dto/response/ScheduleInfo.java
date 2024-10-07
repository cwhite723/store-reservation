package com.hayan.reservation.store.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hayan.reservation.store.domain.WeeklySchedule;

import java.time.LocalTime;

public record ScheduleInfo(Long id,

                           String dayOfWeek,

                           @JsonFormat(pattern = "HH:mm")
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           LocalTime openingTime,

                           @JsonFormat(pattern = "HH:mm")
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           LocalTime closingTime,

                           boolean isClosed) {

    public static ScheduleInfo of(WeeklySchedule weeklySchedule) {

        return new ScheduleInfo(weeklySchedule.getId(),
                weeklySchedule.getDayOfWeek().name(),
                weeklySchedule.getOpeningTime(),
                weeklySchedule.getClosingTime(),
                weeklySchedule.isClosed());
    }
}
