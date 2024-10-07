package com.hayan.reservation.store.controller;

import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.annotation.LoginCheck;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.store.domain.dto.request.CreateScheduleRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateScheduleRequest;
import com.hayan.reservation.store.service.ScheduleService;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /*

    매장 주간 스케줄 등록 api
    POST http://localhost:8080/api/schedule/1

    [
        {
            "dayOfWeek" : "MONDAY",
            "openingTime" : "09:00",
            "closingTime" : "00:00",
            "isClosed" : false
        },
        {
            "dayOfWeek" : "TUESDAY",
            "isClosed" : true
        },
        {
            "dayOfWeek" : "WEDNESDAY",
            "openingTime" : "09:00",
            "closingTime" : "00:00",
            "isClosed" : false
        },
        {
            "dayOfWeek" : "THURSDAY",
            "openingTime" : "09:00",
            "closingTime" : "00:00",
            "isClosed" : false
        },
        {
            "dayOfWeek" : "FRIDAY",
            "openingTime" : "09:00",
            "closingTime" : "02:00",
            "isClosed" : false
        },
        {
            "dayOfWeek" : "SATURDAY",
            "isClosed" : true
        },
        {
            "dayOfWeek" : "SUNDAY",
            "openingTime" : "09:00",
            "closingTime" : "02:00",
            "isClosed" : false
        }
    ]

     */
    @LoginCheck
    @PostMapping("/{store-id}")
    public ApplicationResponse<Void> createAll(@CurrentOwner Owner owner,
                                               @PathVariable("store-id") Long storeId,
                                               @RequestBody List<CreateScheduleRequest> requests) {
        scheduleService.save(owner, storeId, requests);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    스케줄 수정 api (오픈시간, 마감시간, 휴무 등 원하는 값만 넣어서 수정)
    PATCH http://localhost:8080/api/schedule/1

    {
        "openTIme" : 11:00
    }

     */
    @LoginCheck
    @PatchMapping("/{schedule-id}")
    public ApplicationResponse<Void> patch(@CurrentOwner Owner owner,
                                           @PathVariable("schedule-id") Long scheduleId,
                                           @RequestBody UpdateScheduleRequest request) {
        scheduleService.update(owner, scheduleId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
