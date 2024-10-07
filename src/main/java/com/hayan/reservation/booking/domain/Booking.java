package com.hayan.reservation.booking.domain;

import com.hayan.reservation.booking.domain.constant.BookingStatus;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.global.BaseEntity;
import com.hayan.reservation.store.domain.Review;
import com.hayan.reservation.user.domain.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted = false")
public class Booking extends BaseEntity {

    @NonNull
    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(name = "request_message")
    private String requestMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeslot;

    @OneToOne(mappedBy = "booking",
            cascade = CascadeType.PERSIST)
    private Review review;

    public void isVisited() {
        if (this.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new CustomException(ErrorCode.ONLY_COMPLETED_BOOKINGS_CAN_WRITE_REVIEW);
        }
    }
    private void changeStatus(BookingStatus newStatus) {

        if (bookingStatus.canTransitionTo(newStatus)) {
            this.bookingStatus = newStatus;
        } else {
            throw new IllegalStateException(
                    String.format("%s에서 %s로 변경할 수 없습니다.", bookingStatus, newStatus)
            );
        }
    }

    public void approve() {
        changeStatus(BookingStatus.APPROVED);
    }

    public void reject() {
        changeStatus(BookingStatus.REJECTED);
    }

    public void cancel() {
        changeStatus(BookingStatus.CANCELED);
    }

    public void visit() {
        changeStatus(BookingStatus.COMPLETED);
    }

    public void noShow() {
        changeStatus(BookingStatus.NO_SHOW);
    }
}
