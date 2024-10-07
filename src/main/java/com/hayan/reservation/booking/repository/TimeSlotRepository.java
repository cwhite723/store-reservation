package com.hayan.reservation.booking.repository;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    Optional<TimeSlot> findByDaySlotAndTime(DaySlot daySlot, LocalTime time);

    @Modifying
    @Query("UPDATE TimeSlot ts SET ts.deleted = true, ts.deletedAt = NOW() WHERE ts.deleted = false AND ts.id IN :timeslotIds")
    void softDeleteTimeSlots(List<Long> timeslotIds);
}
