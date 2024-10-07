package com.hayan.reservation.store.repository;

import com.hayan.reservation.store.domain.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<WeeklySchedule, Long> {
}
