package com.hayan.reservation.booking.domain;

import com.hayan.reservation.booking.domain.constant.DaySlotStatus;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.global.BaseEntity;
import com.hayan.reservation.store.domain.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "day_slots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted = false")
public class DaySlot extends BaseEntity {

    private LocalDate date;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_slot_status")
    private DaySlotStatus daySlotStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "daySlot",
            cascade = CascadeType.PERSIST)
    private final List<TimeSlot> timeSlots = new ArrayList<>();

    @Builder
    public DaySlot(LocalDate date, DaySlotStatus daySlotStatus, Store store) {
        this.date = date;
        this.daySlotStatus = daySlotStatus;
        this.store = store;
    }

    public void outOfDate() {
        this.daySlotStatus = DaySlotStatus.OUT_OF_DATE;
    }

    public void isAvailable() {
        if (this.daySlotStatus != DaySlotStatus.AVAILABLE) {
            String message = String.format("slot status : %s, value : %s", this.daySlotStatus.name(), this.daySlotStatus.getDescription());
            throw new CustomException(ErrorCode.NO_AVAILABLE_SLOTS, message);
        }
    }
}
