package com.hayan.reservation.store.domain.dto.request;

import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.user.domain.Owner;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record CreateStoreRequest(String name,

                                 String city,

                                 String address,

                                 @Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}$",
                                         message = "전화번호는 9~11자리 숫자와 '-'로 구성되어야 합니다.")
                                 String contact,

                                 String description,

                                 String foodKind,

                                 @Min(1)
                                 int tablePersonMax,

                                 @Min(1)
                                 int tablePersonMin) {

    public Store toEntity(Owner owner) {
        return Store.builder()
                .name(name)
                .city(city)
                .address(address)
                .contact(contact)
                .description(description)
                .foodKind(foodKind)
                .tablePersonMax(tablePersonMax)
                .tablePersonMin(tablePersonMin)
                .owner(owner)
                .build();
    }
}
