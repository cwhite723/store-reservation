package com.hayan.reservation.store.domain.dto.request;

import java.time.LocalTime;
import java.util.Optional;

public record UpdateScheduleRequest(Optional<LocalTime> openingTime,
                                    Optional<LocalTime> closingTime,
                                    boolean isClosed) {
}
