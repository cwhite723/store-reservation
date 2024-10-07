package com.hayan.reservation.booking.service;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.booking.domain.dto.request.CreateOpenRequest;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.user.domain.Owner;

public interface SlotOptionService {
    void save(Owner owner, Long storeId, CreateOpenRequest request);
    SlotOption getByStore(Store store);
}
