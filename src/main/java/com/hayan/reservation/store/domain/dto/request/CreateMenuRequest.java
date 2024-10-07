package com.hayan.reservation.store.domain.dto.request;

import com.hayan.reservation.store.domain.Menu;
import com.hayan.reservation.store.domain.Store;

public record CreateMenuRequest(String name,

                                Integer price,

                                boolean isPopular,

                                String description) {

    public Menu toEntity(Store store) {

        return Menu.builder()
                .name(name)
                .price(price)
                .isPopular(isPopular)
                .description(description)
                .store(store)
                .build();
    }
}
