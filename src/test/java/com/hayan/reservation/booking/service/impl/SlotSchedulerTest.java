package com.hayan.reservation.booking.service.impl;

import com.hayan.reservation.booking.scheduler.SlotOpenScheduler;
import com.hayan.reservation.booking.service.SlotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SlotSchedulerTest {

    @Autowired
    private SlotService slotService; // 실제 SlotService

    @MockBean
    private SlotOpenScheduler slotOpenScheduler; // MockBean으로 변경

    @MockBean
    private Clock clock; // 시간을 Mock으로 만들어서 조작

    @Test
    public void testDailySlotCreationScheduling() throws InterruptedException {
        // 현재 시간을 특정 시간으로 설정
        Instant fixedInstant = LocalDateTime.of(2024, 9, 30, 10, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Mockito.when(clock.instant()).thenReturn(fixedInstant);

        // Daily 스케줄 생성
        Runnable task = () -> slotService.saveDailySlot(1L,false, 1);
        slotOpenScheduler.scheduleRecurringSlotGeneration(task, "0 0 10 * * ?");  // 매일 오전 10시에 실행되는 스케줄

        // 하루 시간이 지난 것으로 Mock 설정
        Instant nextDayInstant = fixedInstant.plus(1, ChronoUnit.DAYS);
        Mockito.when(clock.instant()).thenReturn(nextDayInstant);

        // 스케줄이 실행될 시간을 기다림
        Thread.sleep(1000);

        // 스케줄이 실행되었는지 확인
        verify(slotOpenScheduler, times(1)).scheduleRecurringSlotGeneration(Mockito.any(Runnable.class), Mockito.anyString());
    }
}
