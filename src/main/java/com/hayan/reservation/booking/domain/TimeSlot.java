package com.hayan.reservation.booking.domain;

import com.hayan.reservation.booking.domain.constant.TimeSlotStatus;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "time_slots",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"day_slot_id", "time"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted = false")
public class TimeSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_slot_id")
    private DaySlot daySlot;

    @NonNull
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot_status")
    private TimeSlotStatus timeSlotStatus;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "timeslot",
            cascade = CascadeType.PERSIST)
    private final List<Booking> bookings = new ArrayList<>();

    @Builder
    public TimeSlot(DaySlot daySlot, @NonNull LocalTime time, TimeSlotStatus timeSlotStatus) {
        this.daySlot = daySlot;
        this.time = time;
        this.timeSlotStatus = timeSlotStatus;
    }

    @PrePersist
    @PreUpdate
    public void truncateSeconds() {
        this.time = this.time.withSecond(0).withNano(0);
    }

    public void afterDeadline() {
        this.timeSlotStatus = TimeSlotStatus.AFTER_DEADLINE;
    }

    public void close() {
        this.timeSlotStatus = TimeSlotStatus.CLOSED;
    }

    public void open() {
        this.timeSlotStatus = TimeSlotStatus.AVAILABLE;
    }

    public void isAvailable() {
        if (this.timeSlotStatus != TimeSlotStatus.AVAILABLE) {
            String message = String.format("slot status : %s, value : %s", this.timeSlotStatus.name(), this.timeSlotStatus.getDescription());
            throw new CustomException(ErrorCode.NO_AVAILABLE_SLOTS, message);
        }
    }
}
