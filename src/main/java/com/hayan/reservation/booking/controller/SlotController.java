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

    @LoginCheck
    @PostMapping("/option/monthly/{store-id}")
    public ApplicationResponse<Void> createMonthlyTypeOption(@CurrentOwner Owner owner,
                                                             @PathVariable("store-id") Long storeId,
                                                             @RequestBody CreateMonthlyOpenRequest request) {
        slotOptionService.save(owner, storeId, request);
        slotService.saveMonthlySlot(storeId, true);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PostMapping("/option/weekly/{store-id}")
    public ApplicationResponse<Void> createWeeklyTypeOption(@CurrentOwner Owner owner,
                                                            @PathVariable("store-id") Long storeId,
                                                            @RequestBody CreateWeeklyOpenRequest request) {
        slotOptionService.save(owner, storeId, request);
        slotService.saveWeeklySlot(storeId, true);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PostMapping("/option/daily/{store-id}")
    public ApplicationResponse<Void> createDailyTypeOption(@CurrentOwner Owner owner,
                                                           @PathVariable("store-id") Long storeId,
                                                           @RequestBody CreateDailyOpenRequest request) {
        slotOptionService.save(owner, storeId, request);
        slotService.saveDailySlot(storeId, true, request.availableLimitDay());

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @GetMapping("/{store-id}")
    public ApplicationResponse<List<GetSlotResponse>> getSlots(@PathVariable("store-id") Long storeId,

                                                               @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy/MM/dd")
                                                               LocalDate startDate,

                                                               @RequestParam("end-date") @DateTimeFormat(pattern = "yyyy/MM/dd")
                                                               LocalDate endDate) {
        var slots = slotService.loadSlots(storeId, startDate, endDate);


        return ApplicationResponse.ok(slots, SuccessCode.SUCCESS);
    }

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
