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

    @LoginCheck
    @PostMapping("/{store-id}")
    public ApplicationResponse<Void> createAll(@CurrentOwner Owner owner,
                                               @PathVariable("store-id") Long storeId,
                                               @RequestBody List<CreateScheduleRequest> requests) {
        scheduleService.save(owner, storeId, requests);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @LoginCheck
    @PatchMapping("/{schedule-id}")
    public ApplicationResponse<Void> patch(@CurrentOwner Owner owner,
                                           @PathVariable("schedule-id") Long scheduleId,
                                           @RequestBody UpdateScheduleRequest request) {
        scheduleService.update(owner, scheduleId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
