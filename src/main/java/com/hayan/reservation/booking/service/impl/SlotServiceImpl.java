package com.hayan.reservation.booking.service.impl;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.booking.domain.TimeSlot;
import com.hayan.reservation.booking.domain.constant.DaySlotStatus;
import com.hayan.reservation.booking.domain.constant.TimeSlotStatus;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponse;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponseWithBooking;
import com.hayan.reservation.booking.event.SlotCreateEvent;
import com.hayan.reservation.booking.event.TimeSlotDeadlineEvent;
import com.hayan.reservation.booking.repository.BookingRepository;
import com.hayan.reservation.booking.repository.DaySlotRepository;
import com.hayan.reservation.booking.repository.TimeSlotRepository;
import com.hayan.reservation.booking.service.SlotOptionService;
import com.hayan.reservation.booking.service.SlotService;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Review;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.WeeklySchedule;
import com.hayan.reservation.store.repository.ReviewRepository;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotServiceImpl implements SlotService {

    private final StoreUtility storeUtility;
    private final SlotOptionService slotOptionService;

    private final DaySlotRepository daySlotRepository;
    private final TimeSlotRepository timeSlotRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void saveMonthlySlot(Long storeId, boolean isInitialCall) {
        Store store = storeUtility.getById(storeId);
        SlotOption slotOption = slotOptionService.getByStore(store);

        LocalDate startDate;
        LocalDate endDate;

        if (isInitialCall) {
            startDate = LocalDate.now().plusDays(1);
            endDate = YearMonth.now().plusMonths(1).atEndOfMonth();
        } else {
            startDate = YearMonth.now().plusMonths(1).atDay(1);
            endDate = YearMonth.now().plusMonths(1).atEndOfMonth();
        }

        saveSlots(store, slotOption, startDate, endDate);
        eventPublisher.publishEvent(new SlotCreateEvent(storeId, slotOption));
    }

    @Override
    @Transactional
    public void saveWeeklySlot(Long storeId, boolean isInitialCall) {
        Store store = storeUtility.getById(storeId);
        SlotOption slotOption = slotOptionService.getByStore(store);

        LocalDate startDate;
        LocalDate endDate;

        if (isInitialCall) {
            startDate = LocalDate.now().plusDays(1);
            endDate = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1);
        } else {
            startDate = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1);
            endDate = startDate.with(DayOfWeek.SUNDAY);
        }

        saveSlots(store, slotOption, startDate, endDate);
        eventPublisher.publishEvent(new SlotCreateEvent(storeId, slotOption));
    }

    @Override
    @Transactional
    public void saveDailySlot(Long storeId, boolean isInitialCall, Integer availableLimitDay) {
        Store store = storeUtility.getById(storeId);
        SlotOption slotOption = slotOptionService.getByStore(store);

        LocalDate startDate;
        LocalDate endDate;

        if (isInitialCall) {
            startDate = LocalDate.now().plusDays(1);
            endDate = LocalDate.now().plusDays(availableLimitDay);
        } else {
            startDate = LocalDate.now().plusDays(1);
            endDate = startDate;
        }

        saveSlots(store, slotOption, startDate, endDate);
        eventPublisher.publishEvent(new SlotCreateEvent(storeId, slotOption));
    }

    @Override
    public List<GetSlotResponse> loadSlots(Long storeId, LocalDate startDate, LocalDate endDate) {
        Store store = storeUtility.getById(storeId);

        return daySlotRepository.findSlotsByStoreAndPeriod(store, startDate, endDate);
    }

    @Override
    public List<GetSlotResponseWithBooking> loadSlotsWithBookings(Owner owner, Long storeId, LocalDate startDate, LocalDate endDate) {
        Store store = storeUtility.getById(storeId);
        storeUtility.validateOwner(owner, store);

        return daySlotRepository.findSlotsAndBookingByStoreAndPeriod(store, startDate, endDate);
    }

    @Override
    public TimeSlot findByStoreAndDateTime(Store store, LocalDate date, LocalTime time) {
        DaySlot daySlot = daySlotRepository.findByStoreAndDate(store, date)
                .orElseThrow(() -> new CustomException(ErrorCode.DAY_SLOT_NOT_FOUND));
        daySlot.isAvailable();

        return timeSlotRepository.findByDaySlotAndTime(daySlot, time)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_SLOT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void softDeleteSlots(Long storeId) {
        Store store = storeUtility.getById(storeId);

        List<Long> daySlotIds = store.getDaySlots().stream()
                .map(DaySlot::getId)
                .toList();

        List<Long> timdSlotIds = store.getDaySlots().stream()
                .flatMap(daySlot -> daySlot.getTimeSlots().stream())
                .map(TimeSlot::getId)
                .toList();

        List<Long> reviewIds = store.getReviews().stream()
                .map(Review::getId)
                .toList();

        daySlotRepository.softDeleteDaySlots(daySlotIds);
        timeSlotRepository.softDeleteTimeSlots(timdSlotIds);
        bookingRepository.softDeleteBookingsByTimeSlotIds(timdSlotIds);
        reviewRepository.softDeleteReviews(reviewIds);
    }

    @Override
    @Transactional
    public void checkLimitTablePerHour(TimeSlot timeSlot, int availableTablePerHour) {
        if (timeSlot.getBookings().size() >= availableTablePerHour)
            timeSlot.close();
        else
            timeSlot.open();
    }

    private void saveSlots(Store store, SlotOption slotOption, LocalDate startDate, LocalDate endDate) {

        List<WeeklySchedule> weeklySchedules = store.getWeeklySchedules();

        List<DaySlot> daySlots = createDaySlots(weeklySchedules, store, startDate, endDate);
        daySlotRepository.saveAll(daySlots);
        saveTimeSlots(slotOption, daySlots);
    }

    private List<DaySlot> createDaySlots(List<WeeklySchedule> weeklySchedules, Store store, LocalDate startDate, LocalDate endDate) {

        List<DaySlot> daySlots = new ArrayList<>();
        Optional<LocalDate> lastDaySlotDateOpt = daySlotRepository.findLastDaySlotDateByStore(store.getId());
        LocalDate lastDate = lastDaySlotDateOpt.orElse(startDate.minusDays(1));
        LocalDate currentDate = startDate.isAfter(lastDate) ? startDate : lastDate.plusDays(1);

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            Optional<WeeklySchedule> scheduleOpt = weeklySchedules.stream()
                    .filter(schedule -> schedule.getDayOfWeek().equals(currentDayOfWeek))
                    .findFirst();

            DaySlotStatus status = scheduleOpt.map(schedule -> schedule.isClosed() ? DaySlotStatus.STORE_CLOSED : DaySlotStatus.AVAILABLE)
                    .orElse(DaySlotStatus.STORE_CLOSED);

            DaySlot daySlot = new DaySlot(currentDate, status, store);
            daySlots.add(daySlot);

            currentDate = currentDate.plusDays(1);
        }

        return daySlots;
    }

    private void saveTimeSlots(SlotOption slotOption, List<DaySlot> daySlots) {

        List<TimeSlot> timeSlots = new ArrayList<>();
        List<LocalTime> availableBookingTimes = slotOption.getAvailableBookingTimes();

        for (DaySlot daySlot : daySlots) {
            if (daySlot.getDaySlotStatus().equals(DaySlotStatus.AVAILABLE)) {
                availableBookingTimes.forEach(time -> {
                    TimeSlot timeSlot = new TimeSlot(daySlot, time, TimeSlotStatus.AVAILABLE);
                    timeSlots.add(timeSlot);
                    eventPublisher.publishEvent(new TimeSlotDeadlineEvent(timeSlot, slotOption.getDeadlineHour()));
                });
            }
        }

        timeSlotRepository.saveAll(timeSlots);
    }
}
