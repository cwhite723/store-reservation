package com.hayan.reservation.booking.controller;

import com.hayan.reservation.booking.domain.dto.request.CreateBookingRequest;
import com.hayan.reservation.booking.domain.dto.request.VisitCheckRequest;
import com.hayan.reservation.booking.service.BookingService;
import com.hayan.reservation.common.annotation.CurrentCustomer;
import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.annotation.LoginCheck;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @LoginCheck
    @PostMapping("/{store-id}")
    public ApplicationResponse<Long> create(@CurrentCustomer Customer customer,
                                            @PathVariable("store-id") Long storeId,
                                            @RequestBody CreateBookingRequest request) {

        Long bookingId = bookingService.save(customer, storeId, request);

        return ApplicationResponse.ok(bookingId, SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PatchMapping("/{booking-id}/approve")
    public ApplicationResponse<Void> approve(@CurrentOwner Owner owner,
                                             @PathVariable("booking-id") Long bookingId,
                                             @RequestParam boolean approve) {

        bookingService.approve(owner, bookingId, approve);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PatchMapping("/{booking-id}/cancel")
    public ApplicationResponse<Void> cancel(@CurrentCustomer Customer customer,
                                            @PathVariable("booking-id") Long bookingId) {

        bookingService.cancel(customer, bookingId);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PatchMapping("{booking-id}/visit")
    public ApplicationResponse<Void> visit(@PathVariable("booking-id") Long bookingId,
                                           @RequestBody VisitCheckRequest request) {

        bookingService.visit(bookingId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
