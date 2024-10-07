package com.hayan.reservation.store.service;

import com.hayan.reservation.slice.SliceResponse;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.dto.request.CreateStoreRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateStoreRequest;
import com.hayan.reservation.store.domain.dto.response.ScheduleInfo;
import com.hayan.reservation.store.domain.dto.response.StoreInfo;
import com.hayan.reservation.store.domain.dto.response.StoreInfoResponse;
import com.hayan.reservation.user.domain.Owner;

import java.util.List;

public interface StoreService {
    Long save(Owner owner, CreateStoreRequest request);
    void update(Owner owner, Long storeId, UpdateStoreRequest request);
    SliceResponse<StoreInfoResponse> loadAllStores(int size, Long cursorId);
    StoreInfo loadStoreDetailsById(Long id);
    List<ScheduleInfo> loadSchedules(Long storeId);
    void delete(Owner owner, Long storeId);
}
