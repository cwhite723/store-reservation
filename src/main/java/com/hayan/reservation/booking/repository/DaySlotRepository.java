package com.hayan.reservation.booking.repository;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.store.domain.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DaySlotRepository extends JpaRepository<DaySlot, Long>, CustomDaySlotRepository {
    @Query("SELECT MAX(ds.date) FROM DaySlot ds WHERE ds.store.id = :storeId")
    Optional<LocalDate> findLastDaySlotDateByStore(@Param("storeId") Long storeId);

    Optional<DaySlot> findByStoreAndDate(Store store, LocalDate date);

    @Modifying
    @Query("UPDATE DaySlot ds SET ds.deleted = true, ds.deletedAt = NOW() WHERE ds.deleted = false AND ds.id IN :daySlotIds")
    void softDeleteDaySlots(List<Long> daySlotIds);
}
