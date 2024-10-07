package com.hayan.reservation.store.domain.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record StoreInfoResponse(Long id,
                                String name,
                                String city,
                                String address,
                                String contact,
                                String description) {

    @QueryProjection
    public StoreInfoResponse {
    }
}
