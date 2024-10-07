package com.hayan.reservation.booking.domain;

import com.hayan.reservation.booking.domain.constant.SlotOpenType;
import com.hayan.reservation.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "slot_options")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlotOption {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    private SlotOpenType slotOpenType;

    @Column(name = "open_date")
    private Integer openDate;

    @Enumerated(EnumType.STRING)
    private DayOfWeek openDayOfWeek;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "deadline_hour")
    private int deadlineHour;

    @Column(name = "available_limit_day")
    private Integer availableLimitDay;

    @Column(name = "available_table_per_time")
    private int availableTablePerTime;

    @ElementCollection
    @CollectionTable(name = "available_booking_times", joinColumns = @JoinColumn(name = "slotoption_id"))
    @Column(name = "booking_time")
    private List<LocalTime> availableBookingTimes;

    public static SlotOption typeMonthlyOption(Store store, int openDate, LocalTime openTime, int deadlineHour, int availableTablePerTime, List<LocalTime> availableBookingTimes) {

        return SlotOption.builder()
                .store(store)
                .slotOpenType(SlotOpenType.MONTHLY)
                .openDate(openDate)
                .openTime(openTime)
                .deadlineHour(deadlineHour)
                .availableTablePerTime(availableTablePerTime)
                .availableBookingTimes(availableBookingTimes)
                .build();
    }

    public static SlotOption typeWeeklyOption(Store store, DayOfWeek openDayOfWeek, LocalTime openTime, int deadlineHour, int availableTablePerTime, List<LocalTime> availableBookingTimes) {

        return SlotOption.builder()
                .store(store)
                .slotOpenType(SlotOpenType.WEEKLY)
                .openDayOfWeek(openDayOfWeek)
                .openTime(openTime)
                .deadlineHour(deadlineHour)
                .availableTablePerTime(availableTablePerTime)
                .availableBookingTimes(availableBookingTimes)
                .build();
    }

    public static SlotOption typeDailyOption(Store store, LocalTime openTime, int deadlineHour, int availableTablePerTime, int availableLimitDay, List<LocalTime> availableBookingTimes) {
        return SlotOption.builder()
                .store(store)
                .slotOpenType(SlotOpenType.DAILY)
                .openTime(openTime)
                .deadlineHour(deadlineHour)
                .availableTablePerTime(availableTablePerTime)
                .availableLimitDay(availableLimitDay)
                .availableBookingTimes(availableBookingTimes)
                .build();
    }

}
