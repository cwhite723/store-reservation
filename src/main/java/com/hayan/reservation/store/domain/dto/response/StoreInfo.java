package com.hayan.reservation.store.domain.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record StoreInfo(Long storeId,
                        String name,
                        String contact,
                        String city,
                        String address,
                        String description,
                        int reviewCount,
                        double starPoint,
                        Long ownerId,
                        String ownerName) {
    @QueryProjection
    public StoreInfo {
    }
}
