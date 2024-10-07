package com.hayan.reservation.store.domain.dto.response;

import java.util.List;

public record GetStoreResponse(StoreInfo storeDetails,
                               List<ScheduleInfo> schedules,
                               List<MenuInfo> menus,
                               List<ReviewInfo> reviews) {
}
