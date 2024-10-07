package com.hayan.reservation.booking.repository.impl;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.domain.dto.response.*;
import com.hayan.reservation.booking.repository.CustomDaySlotRepository;
import com.hayan.reservation.store.domain.Store;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hayan.reservation.booking.domain.QBooking.booking;
import static com.hayan.reservation.booking.domain.QDaySlot.daySlot;
import static com.hayan.reservation.booking.domain.QTimeSlot.timeSlot;
import static com.hayan.reservation.user.domain.QCustomer.customer;

@RequiredArgsConstructor
public class CustomDaySlotRepositoryImpl implements CustomDaySlotRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetSlotResponse> findSlotsByStoreAndPeriod(Store store, LocalDate startDate, LocalDate endDate) {
        List<DaySlot> daySlots = queryFactory
                .selectFrom(daySlot)
                .distinct()
                .leftJoin(daySlot.timeSlots, timeSlot).fetchJoin()
                .where(daySlot.store.eq(store)
                        .and(daySlot.date.between(startDate, endDate)))
                .fetch();

        return daySlots.stream()
                .map(GetSlotResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetSlotResponseWithBooking> findSlotsAndBookingByStoreAndPeriod(Store store, LocalDate startDate, LocalDate endDate) {
        List<DaySlot> daySlots = queryFactory
                .selectFrom(daySlot)
                .leftJoin(daySlot.timeSlots, timeSlot).fetchJoin()
                .where(daySlot.store.eq(store)
                        .and(daySlot.date.between(startDate, endDate)))
                .distinct()
                .fetch();

        List<Long> timeSlotIds = daySlots.stream()
                .flatMap(ds -> ds.getTimeSlots().stream())
                .map(TimeSlot::getId)
                .collect(Collectors.toList());

        // Booking과 TimeSlot을 정확하게 매핑하는 map을 생성
        Map<Long, List<BookingInfo>> bookingMap = findBookingsByTimeSlotIds(timeSlotIds);

        return daySlots.stream()
                .map(ds -> new GetSlotResponseWithBooking(
                        DaySlotInfo.of(ds),
                        ds.getTimeSlots().stream()
                                .map(ts -> new TimeSlotInfoWithBooking(
                                        ts.getId(),
                                        ts.getTime(),
                                        ts.getTimeSlotStatus(),
                                        bookingMap.getOrDefault(ts.getId(), List.of()) // TimeSlot ID로 정확하게 매핑
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 정확한 TimeSlot에 매핑된 Booking 정보를 가져오는 메서드
    private Map<Long, List<BookingInfo>> findBookingsByTimeSlotIds(List<Long> timeSlotIds) {
        List<Tuple> bookingTuples = queryFactory
                .select(booking.timeslot.id, new QBookingInfo(
                        booking.id,
                        customer.name,
                        customer.contact,
                        booking.guestCount,
                        booking.requestMessage,
                        booking.bookingStatus
                ))
                .from(booking)
                .innerJoin(booking.customer, customer)
                .where(booking.timeslot.id.in(timeSlotIds))  // 정확한 TimeSlot에 해당하는 Booking만 조회
                .fetch();

        // TimeSlot ID로 Booking을 그룹핑
        return bookingTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(booking.timeslot.id),  // TimeSlot ID로 그룹핑
                        Collectors.mapping(
                                tuple -> tuple.get(1, BookingInfo.class),  // BookingInfo로 매핑
                                Collectors.toList()
                        )
                ));
    }
}
