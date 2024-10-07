package com.hayan.reservation.store.domain.dto.response;

public record MenuInfo(Long id,
                       String name,
                       Integer price,
                       boolean isPopular,
                       String description) {
}
