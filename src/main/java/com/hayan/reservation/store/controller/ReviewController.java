package com.hayan.reservation.store.controller;

import com.hayan.reservation.common.annotation.CurrentCustomer;
import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.annotation.LoginCheck;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.store.domain.dto.request.ReviewRequest;
import com.hayan.reservation.store.service.ReviewService;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @LoginCheck
    @PostMapping("/{booking-id}")
    public ApplicationResponse<Long> create(@CurrentCustomer Customer customer,
                                            @PathVariable("booking-id") Long bookingId,
                                            @RequestBody ReviewRequest request) {

        Long reviewId = reviewService.save(customer, bookingId, request);

        return ApplicationResponse.ok(reviewId, SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PatchMapping("/{review-id}")
    public ApplicationResponse<Void> patch(@CurrentCustomer Customer customer,
                                           @PathVariable("review-id") Long reviewId,
                                           @RequestBody ReviewRequest request) {

        reviewService.update(customer, reviewId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @DeleteMapping("/customer/{review-id}")
    public ApplicationResponse<Void> delete(@CurrentCustomer Customer customer,
                                            @PathVariable("review-id") Long reviewId) {

        reviewService.deleteByAuthor(customer, reviewId);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @DeleteMapping("/owner/{review-id}")
    public ApplicationResponse<String> delete(@CurrentOwner Owner owner,
                                              @PathVariable("review-id") Long reviewId,
                                              @RequestBody String reason) {

        reviewService.deleteByOwner(owner, reviewId, reason);

        return ApplicationResponse.ok(reason, SuccessCode.SUCCESS);
    }
}
