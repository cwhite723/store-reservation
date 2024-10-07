package com.hayan.reservation.booking.domain.dto.request;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.domain.constant.BookingStatus;
import com.hayan.reservation.user.domain.Customer;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateBookingRequest(LocalDate date,
                                   LocalTime time,
                                   Integer guestCount,
                                   String requestMessage) {

    public Booking toEntity(Customer customer, TimeSlot timeslot) {
        return Booking.builder()
                .guestCount(guestCount)
                .requestMessage(requestMessage)
                .bookingStatus(BookingStatus.PENDING)
                .customer(customer)
                .timeslot(timeslot)
                .build();
    }
}
