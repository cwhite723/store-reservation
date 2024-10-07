package com.hayan.reservation.booking.service;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.domain.dto.request.CreateBookingRequest;
import com.hayan.reservation.booking.domain.dto.request.VisitCheckRequest;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;

public interface BookingService {
    Long save(Customer customer, Long storeId, CreateBookingRequest request);
    void validateExistsBooking(Store store);
    void approve(Owner owner, Long bookingId, boolean approve);
    void cancel(Customer customer, Long bookingId);
    void visit(Long bookingId, VisitCheckRequest request);
    void noShow(Booking booking);
    Booking getById(Long bookingId);
    void validateCustomer(Customer loginCustomer, Customer bookingCustomer);
    Store getStoreByBooking(Booking booking);
}
