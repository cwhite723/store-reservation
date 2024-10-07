package com.hayan.reservation.booking.event.listener;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.booking.domain.constant.SlotOpenType;
import com.hayan.reservation.booking.event.SlotCreateEvent;
import com.hayan.reservation.booking.scheduler.SlotOpenScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlotCreateEventListener {

    private final SlotOpenScheduler slotOpenScheduler;

    @EventListener
    public void handleSlotCreatedEvent(SlotCreateEvent event) {
        Long storeId = event.storeId();
        SlotOption slotOption = event.slotOption();
        SlotOpenType slotOpenType = slotOption.getSlotOpenType();

        String cronExpression = slotOpenScheduler.getCronExpressionForSlotOption(slotOpenType, slotOption);
        slotOpenScheduler.scheduleRecurringSlotGeneration(
                () -> slotOpenScheduler.executeSlotCreationTask(storeId, slotOpenType),
                cronExpression
        );
    }
}