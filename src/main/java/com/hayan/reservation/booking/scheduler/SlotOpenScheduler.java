package com.hayan.reservation.booking.scheduler;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.booking.domain.constant.SlotOpenType;
import com.hayan.reservation.booking.service.SlotService;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SlotOpenScheduler {

    private final TaskScheduler taskScheduler;
    private final SlotService slotService;

    public ScheduledFuture<?> scheduleRecurringSlotGeneration(Runnable slotCreationTask, String cronExpression) {
        return taskScheduler.schedule(slotCreationTask, new CronTrigger(cronExpression));
    }

    public String getCronExpressionForSlotOption(SlotOpenType slotOpenType, SlotOption slotOption) {
        LocalTime openTime = slotOption.getOpenTime();

        switch (slotOpenType) {
            case MONTHLY -> {
                int openDate = slotOption.getOpenDate();
                return String.format("0 %d %d %d * ?", openTime.getMinute(), openTime.getHour(), openDate);
            }
            case WEEKLY -> {
                int dayOfWeek = slotOption.getOpenDayOfWeek().getValue();
                return String.format("0 %d %d * * %d", openTime.getMinute(), openTime.getHour(), dayOfWeek);
            }
            case DAILY -> {
                return String.format("0 %d %d * * ?", openTime.getMinute(), openTime.getHour());
            }
            default -> throw new CustomException(ErrorCode.SLOT_OPTION_NOT_FOUND);
        }
    }

    public void executeSlotCreationTask(Long storeId, SlotOpenType slotOpenType) {
        switch (slotOpenType) {
            case MONTHLY -> {
                slotService.saveMonthlySlot(storeId, false);
            }
            case WEEKLY -> {
                slotService.saveWeeklySlot(storeId, false);
            }
            case DAILY -> {
                slotService.saveDailySlot(storeId, false, null);
            }
        }
    }
}

