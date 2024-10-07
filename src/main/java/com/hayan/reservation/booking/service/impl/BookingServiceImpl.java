package com.hayan.reservation.booking.service.impl;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.domain.constant.BookingStatus;
import com.hayan.reservation.booking.domain.dto.request.CreateBookingRequest;
import com.hayan.reservation.booking.domain.dto.request.VisitCheckRequest;
import com.hayan.reservation.booking.event.VisitCheckEvent;
import com.hayan.reservation.booking.repository.BookingRepository;
import com.hayan.reservation.booking.service.BookingService;
import com.hayan.reservation.booking.service.SlotService;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final StoreUtility storeUtility;
    private final SlotService slotService;

    private final BookingRepository bookingRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Long save(Customer customer, Long storeId, CreateBookingRequest request) {

        Store store = storeUtility.getById(storeId);
        TimeSlot timeSlot = slotService.findByStoreAndDateTime(store, request.date(), request.time());
        timeSlot.isAvailable();

        Booking booking = request.toEntity(customer, timeSlot);
        bookingRepository.save(booking);

        return booking.getId();
    }

    @Override
    public void validateExistsBooking(Store store) {

        if (bookingRepository.existsByStoreAndBookingStatus(store, BookingStatus.APPROVED)) {
            throw new CustomException(ErrorCode.ACTIVE_BOOKINGS_EXIST);
        }
    }

    @Override
    @Transactional
    public void approve(Owner owner, Long bookingId, boolean approve) {

        Booking booking = getById(bookingId);
        Store store = getStoreByBooking(booking);
        storeUtility.validateOwner(owner, store);

        if (approve) {
            booking.approve();
            slotService.checkLimitTablePerHour(booking.getTimeslot(), store.getSlotOption().getAvailableTablePerTime());
            eventPublisher.publishEvent(new VisitCheckEvent(booking));
        }
        else booking.reject();
    }

    @Override
    @Transactional
    public void cancel(Customer customer, Long bookingId) {

        Booking booking = getById(bookingId);
        Store store = getStoreByBooking(booking);
        validateCustomer(customer, booking.getCustomer());

        booking.cancel();
        slotService.checkLimitTablePerHour(booking.getTimeslot(), store.getSlotOption().getAvailableTablePerTime());
    }

    @Override
    @Transactional
    public void visit(Long bookingId, VisitCheckRequest request) {

        Booking booking = getById(bookingId);
        validateCustomerInfo(booking.getCustomer(), request.name(), request.contact());

        LocalTime bookingTime = booking.getTimeslot().getTime();
        LocalTime availableVisitCheckTime = bookingTime.minusMinutes(10);
        if (request.currentTime().isBefore(availableVisitCheckTime) || request.currentTime().isAfter(bookingTime))
            throw new CustomException(ErrorCode.NOT_VISIT_CONFIRMATION_TIME);

        booking.visit();
    }

    @Override
    @Transactional
    public void noShow(Booking booking) {

        if (booking.getBookingStatus() == BookingStatus.APPROVED) {
            booking.noShow();
        }
    }

    @Override
    public Booking getById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));
    }

    @Override
    public void validateCustomer(Customer loginCustomer, Customer bookingCustomer) {

        if (!Objects.equals(loginCustomer, bookingCustomer))
            throw new CustomException(ErrorCode.NOT_CUSTOMER);
    }

    @Override
    public Store getStoreByBooking(Booking booking) {

        return bookingRepository.findStoreByBooking(booking)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private void validateCustomerInfo(Customer bookingCustomer, String name, String contact) {

        if (!name.equals(bookingCustomer.getName()) || !contact.equals(bookingCustomer.getContact())) {
            throw new CustomException(ErrorCode.NOT_CUSTOMER);
        }
    }
}
