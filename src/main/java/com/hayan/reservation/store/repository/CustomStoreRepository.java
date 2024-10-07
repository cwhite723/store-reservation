package com.hayan.reservation.store.repository;

import com.hayan.reservation.store.domain.dto.response.StoreInfo;
import com.hayan.reservation.store.domain.dto.response.StoreInfoResponse;
import org.springframework.data.domain.Slice;

public interface CustomStoreRepository {

    Slice<StoreInfoResponse> getAllStores(int size, Long cursorId);
    StoreInfo getStoreDetails(Long id);
}
