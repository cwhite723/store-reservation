package com.hayan.reservation.store.service;

import com.hayan.reservation.store.domain.dto.request.CreateScheduleRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateScheduleRequest;
import com.hayan.reservation.user.domain.Owner;

import java.util.List;

public interface ScheduleService {
    void save(Owner owner, Long storeId, List<CreateScheduleRequest> requests);
    void update(Owner owner, Long scheduleId, UpdateScheduleRequest request);
}
