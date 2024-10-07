package com.hayan.reservation.store.domain;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.global.BaseEntity;
import com.hayan.reservation.user.domain.Customer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Id
    private Long id;

    private String content;

    private int starPoint;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Builder
    public Review(String content, int starPoint, Store store, Booking booking, Customer customer) {
        this.content = content;
        this.starPoint = starPoint;
        this.store = store;
        this.booking = booking;
        this.customer = customer;
    }

    public void update(String content, int starPoint) {
        this.content = content;
        this.starPoint = starPoint;
    }
}
