package com.hayan.reservation.store.service;

import com.hayan.reservation.store.domain.dto.request.ReviewRequest;
import com.hayan.reservation.store.domain.dto.response.ReviewInfo;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;

import java.util.List;

public interface ReviewService {
    Long save(Customer customer, Long bookingId, ReviewRequest request);
    void update(Customer customer, Long reviewId, ReviewRequest request);
    void deleteByAuthor(Customer customer, Long reviewId);
    void deleteByOwner(Owner owner, Long reviewId, String reason);
    List<ReviewInfo> loadReviews(Long storeId);
}
