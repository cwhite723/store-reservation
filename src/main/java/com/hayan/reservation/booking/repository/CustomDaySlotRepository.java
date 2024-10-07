package com.hayan.reservation.booking.repository;

import com.hayan.reservation.booking.domain.dto.response.GetSlotResponse;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponseWithBooking;
import com.hayan.reservation.store.domain.Store;

import java.time.LocalDate;
import java.util.List;

public interface CustomDaySlotRepository {
    List<GetSlotResponse> findSlotsByStoreAndPeriod(Store store, LocalDate startDate, LocalDate endDate);
    List<GetSlotResponseWithBooking> findSlotsAndBookingByStoreAndPeriod(Store store, LocalDate startDate, LocalDate endDate);
}
