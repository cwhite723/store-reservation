package com.hayan.reservation.store.domain.dto.request;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.store.domain.Review;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.user.domain.Customer;

public record ReviewRequest(String content,

                            int starPoint) {

    public Review toEntity(Store store, Booking booking, Customer customer) {

        return Review.builder()
                .content(content)
                .starPoint(starPoint)
                .booking(booking)
                .customer(customer)
                .store(store)
                .build();
    }
}
