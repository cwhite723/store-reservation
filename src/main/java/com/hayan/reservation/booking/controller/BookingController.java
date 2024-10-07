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

    /*

    새로운 예약을 생성하는 api
    POST http://localhost:8080/api/booking/1

    {
    "date" : "2024/10/09",
    "time" : "19:00",
    "guestCount" : 3,
    "requestMessage" : "아기의자 한 개 필요해요!"
    }

     */
    @LoginCheck
    @PostMapping("/{store-id}")
    public ApplicationResponse<Long> create(@CurrentCustomer Customer customer,
                                            @PathVariable("store-id") Long storeId,
                                            @RequestBody CreateBookingRequest request) {

        Long bookingId = bookingService.save(customer, storeId, request);

        return ApplicationResponse.ok(bookingId, SuccessCode.SUCCESS);
    }

    /*

    점주가 예약을 승인/거절하는 api
    PATCH http://localhost:8080/api/booking/5/approve?approve=true

     */
    @LoginCheck
    @PatchMapping("/{booking-id}/approve")
    public ApplicationResponse<Void> approve(@CurrentOwner Owner owner,
                                             @PathVariable("booking-id") Long bookingId,
                                             @RequestParam boolean approve) {

        bookingService.approve(owner, bookingId, approve);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    고객이 예약을 취소하는 api
    PATCH http://localhost:8080/api/booking/5/cancel

     */
    @LoginCheck
    @PatchMapping("/{booking-id}/cancel")
    public ApplicationResponse<Void> cancel(@CurrentCustomer Customer customer,
                                            @PathVariable("booking-id") Long bookingId) {

        bookingService.cancel(customer, bookingId);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    방문 확인 api
        - 실제로 현장에서 키오스크로 확인한다고 생각했을 때 로그인을 하기보단 개인정보 입력이 더 자연스럽다고 생각해서
        예약번호와 현재 시간, 개인정보를 받는 형태로 구현
    PATCH http://localhost:8080/api/booking/5/visit

    {
    "currentTime" : "18:55",
    "name" : "조하얀2",
    "contact" : "010-1234-5678"
    }

     */
    @LoginCheck
    @PatchMapping("{booking-id}/visit")
    public ApplicationResponse<Void> visit(@PathVariable("booking-id") Long bookingId,
                                           @RequestBody VisitCheckRequest request) {

        bookingService.visit(bookingId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
