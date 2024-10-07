package com.hayan.reservation.store.service.impl;

import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.WeeklySchedule;
import com.hayan.reservation.store.domain.dto.request.CreateScheduleRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateScheduleRequest;
import com.hayan.reservation.store.repository.ScheduleRepository;
import com.hayan.reservation.store.service.ScheduleService;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final StoreUtility storeUtility;

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void save(Owner owner, Long storeId, List<CreateScheduleRequest> requests) {
        Store store = storeUtility.getById(storeId);
        storeUtility.validateOwner(owner, store);

        List<WeeklySchedule> weeklySchedules = requests.stream()
                .map(request -> request.toEntity(store))
                .toList();

        scheduleRepository.saveAll(weeklySchedules);
    }

    @Override
    @Transactional
    public void update(Owner owner, Long scheduleId, UpdateScheduleRequest request) {
        WeeklySchedule weeklySchedule = getById(scheduleId);
        storeUtility.validateOwner(owner, weeklySchedule.getStore());

        weeklySchedule.update(request.openingTime(),
                request.closingTime(),
                request.isClosed());
    }

    private WeeklySchedule getById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}
