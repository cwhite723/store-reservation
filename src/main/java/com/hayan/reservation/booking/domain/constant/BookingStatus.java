package com.hayan.reservation.booking.domain.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    PENDING("예약 승인 대기 중") {
        @Override
        public Set<BookingStatus> allowedNextStatuses() {
            return EnumSet.of(APPROVED, REJECTED, CANCELED);
        }
    },
    APPROVED("예약 완료") {
        @Override
        public Set<BookingStatus> allowedNextStatuses() {
            return EnumSet.of(COMPLETED, NO_SHOW);
        }
    },
    COMPLETED("방문 완료") {
        @Override
        public Set<BookingStatus> allowedNextStatuses() {
            return EnumSet.noneOf(BookingStatus.class);
        }
    },
    NO_SHOW("노쇼") {
        @Override
        public Set<BookingStatus> allowedNextStatuses() {
            return EnumSet.noneOf(BookingStatus.class);
        }
    },
    CANCELED("예약 취소 (고객 요청)") {
        @Override
        public Set<BookingStatus> allowedNextStatuses() {
            return EnumSet.noneOf(BookingStatus.class);
        }
    },
    REJECTED("예약 거절 (매장 요청)") {
        @Override
        public Set<BookingStatus> allowedNextStatuses() {
            return EnumSet.noneOf(BookingStatus.class);
        }
    };

    @JsonValue
    private final String description;

    public abstract Set<BookingStatus> allowedNextStatuses();

    public boolean canTransitionTo(BookingStatus newStatus) {
        return allowedNextStatuses().contains(newStatus);
    }
}