package com.hayan.reservation.booking.domain.dto.request;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.store.domain.Store;

public interface CreateOpenRequest {

    SlotOption toEntity(Store store);
}
