package com.hayan.reservation.booking.service;

import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponse;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponseWithBooking;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.user.domain.Owner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SlotService {
    void saveMonthlySlot(Long storeId, boolean isInitialCall);
    void saveWeeklySlot(Long storeId, boolean isInitialCall);
    void saveDailySlot(Long storeId, boolean isInitialCallInteger, Integer availableLimitDays);
    List<GetSlotResponse> loadSlots(Long storeId, LocalDate startDate, LocalDate endDate);
    List<GetSlotResponseWithBooking> loadSlotsWithBookings(Owner owner, Long storeId, LocalDate startDate, LocalDate endDate);
    TimeSlot findByStoreAndDateTime(Store store, LocalDate date, LocalTime time);
    void softDeleteSlots(Long id);
    void checkLimitTablePerHour(TimeSlot timeSlot, int availableTablePerHour);
}
