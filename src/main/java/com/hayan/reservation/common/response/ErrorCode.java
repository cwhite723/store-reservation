package com.hayan.reservation.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResponseCode {
    // 400 Bad Request
    REQUEST_VALIDATION_FAIL(BAD_REQUEST, "400", "잘못된 요청 값입니다."),
    USER_ALREADY_EXISTS(BAD_REQUEST, "400", "이미 가입된 회원입니다."),
    INVALID_USER_TYPE(BAD_REQUEST, "400", "올바르지 않은 회원 타입입니다."),
    INVALID_DAY_OF_WEEK(BAD_REQUEST, "400", "올바르지 않은 요일 값입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "인증에 실패했습니다."),

    // 403 Forbidden
    OWNER_PRIVILEGE_REQUIRED(FORBIDDEN, "403", "Owner 회원만 매장 등록이 가능합니다."),
    CUSTOMER_PRIVILEGE_REQUIRED(FORBIDDEN, "403", "Customer 회원만 예약이 가능합니다."),
    NOT_OWNER(FORBIDDEN, "403", "매장의 소유주가 아닙니다."),
    NOT_CUSTOMER(FORBIDDEN, "403", "예약한 계정이 아닙니다."),
    NOT_AUTHOR(FORBIDDEN, "403", "작성자가 아닙니다."),

    // 404 Not Found
    USER_NOT_FOUND(NOT_FOUND, "404", "회원이 존재하지 않습니다."),
    STORE_NOT_FOUND(NOT_FOUND, "404", "매장이 존재하지 않습니다."),
    MENU_NOT_FOUND(NOT_FOUND, "404", "메뉴가 존재하지 않습니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, "404", "스케줄이 존재하지 않습니다."),
    SLOT_OPTION_NOT_FOUND(NOT_FOUND, "404", "슬롯 옵션이 존재하지 않습니다."),
    DAY_SLOT_NOT_FOUND(NOT_FOUND, "404", "데이 슬롯이 존재하지 않습니다."),
    TIME_SLOT_NOT_FOUND(NOT_FOUND, "404", "타임 슬롯이 존재하지 않습니다."),
    BOOKING_NOT_FOUND(NOT_FOUND, "404", "예약이 존재하지 않습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "404", "리뷰가 존재하지 않습니다."),

    // 409 Conflict
    NO_AVAILABLE_SLOTS(CONFLICT, "409", "예약 가능한 슬롯이 없습니다."),
    ACTIVE_BOOKINGS_EXIST(CONFLICT, "409", "진행 중인 예약이 있으면 매장을 삭제할 수 없습니다."),
    NOT_VISIT_CONFIRMATION_TIME(CONFLICT, "409", "방문 확인은 예약 시간 10분 전부터 가능합니다."),
    ONLY_COMPLETED_BOOKINGS_CAN_WRITE_REVIEW(CONFLICT, "409", "방문 완료일 때만 후기 작성이 가능합니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
