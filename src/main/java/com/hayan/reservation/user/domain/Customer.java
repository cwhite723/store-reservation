package com.hayan.reservation.user.domain;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.store.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@DiscriminatorValue("customer")
@NoArgsConstructor
public class Customer extends User {

    @OneToMany(mappedBy = "customer",
            cascade = CascadeType.PERSIST)
    private final List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "customer",
            cascade = CascadeType.PERSIST)
    private final List<Review> reviews = new ArrayList<>();

    @Builder
    public Customer(String name, String contact, String username, String password, Set<String> roles) {
        super(name, contact, username, password, roles);
    }
}
