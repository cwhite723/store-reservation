package com.hayan.reservation.booking.domain.dto.request;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.store.domain.Store;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalTime;
import java.util.List;

public record CreateMonthlyOpenRequest(@Min(value = 1, message = "예약 오픈일자는 매월 1일부터 28일까지 지정 가능합니다.")
                                       @Max(value = 28, message = "예약 오픈일자는 매월 1일부터 28일까지 지정 가능합니다.")
                                       int openDate,

                                       LocalTime openTime,

                                       @Min(value = 1, message = "예약 마감은 최소 1시간 전입니다.")
                                       @Max(value = 23, message = "예약 마감은 최대 23시간 전입니다.")
                                       int deadlineHour,

                                       int availableTablePerTime,

                                       List<LocalTime> availableBookingTimes) implements CreateOpenRequest {

    @Override
    public SlotOption toEntity(Store store) {

        return SlotOption.typeMonthlyOption(store, openDate, openTime, deadlineHour, availableTablePerTime, availableBookingTimes);
    }
}
