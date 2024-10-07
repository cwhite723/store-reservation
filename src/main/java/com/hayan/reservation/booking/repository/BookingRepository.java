package com.hayan.reservation.booking.repository;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.domain.constant.BookingStatus;
import com.hayan.reservation.store.domain.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "JOIN b.timeslot ts " +
            "JOIN ts.daySlot ds " +
            "WHERE ds.store = :store AND b.bookingStatus = :bookingStatus")
    boolean existsByStoreAndBookingStatus(@Param("store") Store store, @Param("bookingStatus") BookingStatus bookingStatus);

    @Query("SELECT ds.store FROM Booking b " +
            "JOIN b.timeslot ts " +
            "JOIN ts.daySlot ds " +
            "WHERE b = :booking")
    Optional<Store> findStoreByBooking(@Param("booking") Booking booking);

    @Modifying
    @Query("UPDATE Booking b SET b.deleted = true, b.deletedAt = NOW() WHERE b.deleted = false AND b.timeslot.id IN :timeSlotIds")
    void softDeleteBookingsByTimeSlotIds(List<Long> timeSlotIds);
}
