package com.hayan.reservation.booking.controller;

import com.hayan.reservation.booking.domain.dto.request.CreateDailyOpenRequest;
import com.hayan.reservation.booking.domain.dto.request.CreateMonthlyOpenRequest;
import com.hayan.reservation.booking.domain.dto.request.CreateWeeklyOpenRequest;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponse;
import com.hayan.reservation.booking.domain.dto.response.GetSlotResponseWithBooking;
import com.hayan.reservation.booking.service.SlotOptionService;
import com.hayan.reservation.booking.service.SlotService;
import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.annotation.LoginCheck;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slot")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;
    private final SlotOptionService slotOptionService;

    /*

    매월 특정 날짜에 예약을 오픈하는 매장의 슬롯옵션 등록
    POST http://localhost:8080/api/slot/option/monthly/1

    {
    "openDate" : 1,
    "openTime" : "12:00",
    "deadlineHour" : 3,
    "availableTablePerTime" : 3,
    "availableBookingTimes" : [
        "10:00",
        "11:30",
        "17:30",
        "19:00",
        "20:30"
        ]
    }

     */
    @LoginCheck
    @PostMapping("/option/monthly/{store-id}")
    public ApplicationResponse<Void> createMonthlyTypeOption(@CurrentOwner Owner owner,
                                                             @PathVariable("store-id") Long storeId,
                                                             @RequestBody CreateMonthlyOpenRequest request) {
        slotOptionService.save(owner, storeId, request);
        slotService.saveMonthlySlot(storeId, true);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    매주 특정 요일에 예약을 오픈하는 매장의 슬롯옵션 등록
    POST http://localhost:8080/api/slot/option/weekly/1

    {
    "openDayOfWeek" : MONDAY,
    "openTime" : "12:00",
    "deadlineHour" : 3,
    "availableTablePerTime" : 3,
    "availableBookingTimes" : [
        "10:00",
        "11:30",
        "17:30",
        "19:00",
        "20:30"
        ]
    }

     */
    @LoginCheck
    @PostMapping("/option/weekly/{store-id}")
    public ApplicationResponse<Void> createWeeklyTypeOption(@CurrentOwner Owner owner,
                                                            @PathVariable("store-id") Long storeId,
                                                            @RequestBody CreateWeeklyOpenRequest request) {
        slotOptionService.save(owner, storeId, request);
        slotService.saveWeeklySlot(storeId, true);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    매일 익일 예약을 오픈하는 매장의 슬롯옵션 등록
    POST http://localhost:8080/api/slot/option/daily/1

    {
    "openTime" : "12:00",
    "deadlineHour" : 3,
    "availableLimitDay" : 90,
    "availableTablePerTime" : 3,
    "availableBookingTimes" : [
        "10:00",
        "11:30",
        "17:30",
        "19:00",
        "20:30"
        ]
    }

     */
    @LoginCheck
    @PostMapping("/option/daily/{store-id}")
    public ApplicationResponse<Void> createDailyTypeOption(@CurrentOwner Owner owner,
                                                           @PathVariable("store-id") Long storeId,
                                                           @RequestBody CreateDailyOpenRequest request) {
        slotOptionService.save(owner, storeId, request);
        slotService.saveDailySlot(storeId, true, request.availableLimitDay());

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    고객이 매장의 슬롯들을 조회하는 api
    GET http://localhost:8080/api/slot/1?start-date=2024/10/01&end-date=2024/10/11

     */
    @GetMapping("/{store-id}")
    public ApplicationResponse<List<GetSlotResponse>> getSlots(@PathVariable("store-id") Long storeId,

                                                               @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy/MM/dd")
                                                               LocalDate startDate,

                                                               @RequestParam("end-date") @DateTimeFormat(pattern = "yyyy/MM/dd")
                                                               LocalDate endDate) {
        var slots = slotService.loadSlots(storeId, startDate, endDate);


        return ApplicationResponse.ok(slots, SuccessCode.SUCCESS);
    }

    /*

    점주가 매장의 슬롯들을 조회하는 api
        - 고객이 요청할 때와 다르게 예약 리스트도 response에 포함
    GET http://localhost:8080/api/slot/owner/1?start-date=2024/10/01&end-date=2024/10/11

     */
    @LoginCheck
    @GetMapping("/owner/{store-id}")
    public ApplicationResponse<List<GetSlotResponseWithBooking>> getSlots(@CurrentOwner Owner owner,

                                                                          @PathVariable("store-id") Long storeId,

                                                                          @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy/MM/dd")
                                                                          LocalDate startDate,

                                                                          @RequestParam("end-date") @DateTimeFormat(pattern = "yyyy/MM/dd")
                                                                          LocalDate endDate) {
        var slots = slotService.loadSlotsWithBookings(owner, storeId, startDate, endDate);

        return ApplicationResponse.ok(slots, SuccessCode.SUCCESS);
    }
}
