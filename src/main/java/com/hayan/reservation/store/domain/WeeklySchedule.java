package com.hayan.reservation.store.domain;

import com.hayan.reservation.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

@Entity
@Builder
@Getter
@Table(name = "weekly_schedule")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklySchedule extends BaseEntity {

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime openingTime;
    private LocalTime closingTime;

    private boolean isClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void update(Optional<LocalTime> openingTime, Optional<LocalTime> closingTime, boolean isClosed) {
        openingTime.ifPresent(newOpeningTime -> this.openingTime = newOpeningTime);
        closingTime.ifPresent(newClosingTime -> this.closingTime = newClosingTime);
        this.isClosed = isClosed;
    }
}