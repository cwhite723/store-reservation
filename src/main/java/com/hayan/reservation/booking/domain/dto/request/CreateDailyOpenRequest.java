package com.hayan.reservation.booking.domain.dto.request;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.store.domain.Store;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalTime;
import java.util.List;

public record CreateDailyOpenRequest(LocalTime openTime,

                                     @Min(value = 1, message = "예약 마감은 최소 1시간 전입니다.")
                                     @Max(value = 23, message = "예약 마감은 최대 23시간 전입니다.")
                                     int deadlineHour,

                                     @Max(value = 90, message = "예약 가능한 최대 기간은 90일입니다.")
                                     int availableLimitDay,

                                     int availableTablePerTime,

                                     List<LocalTime> availableBookingTimes) implements CreateOpenRequest {

    @Override
    public SlotOption toEntity(Store store) {
        return SlotOption.typeDailyOption(store, openTime, deadlineHour, availableTablePerTime, availableLimitDay, availableBookingTimes);
    }
}