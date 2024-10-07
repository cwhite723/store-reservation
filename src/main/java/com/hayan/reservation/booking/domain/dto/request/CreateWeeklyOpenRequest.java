package com.hayan.reservation.booking.domain.dto.request;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.store.domain.Store;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record CreateWeeklyOpenRequest(DayOfWeek openDayOfWeek,

                                      LocalTime openTime,

                                      @Min(value = 1, message = "예약 마감은 최소 1시간 전입니다.")
                                      @Max(value = 23, message = "예약 마감은 최대 23시간 전입니다.")
                                      int deadlineHour,

                                      int availableTablePerTime,

                                      List<LocalTime> availableBookingTimes) implements CreateOpenRequest {

    @Override
    public SlotOption toEntity(Store store) {
        return SlotOption.typeWeeklyOption(store, openDayOfWeek, openTime, deadlineHour, availableTablePerTime, availableBookingTimes);
    }
}
