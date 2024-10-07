package com.hayan.reservation.store.service.impl;

import com.hayan.reservation.booking.service.BookingService;
import com.hayan.reservation.booking.service.SlotService;
import com.hayan.reservation.slice.SliceResponse;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.dto.request.CreateStoreRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateStoreRequest;
import com.hayan.reservation.store.domain.dto.response.ScheduleInfo;
import com.hayan.reservation.store.domain.dto.response.StoreInfo;
import com.hayan.reservation.store.domain.dto.response.StoreInfoResponse;
import com.hayan.reservation.store.repository.StoreRepository;
import com.hayan.reservation.store.service.StoreService;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreUtility storeUtility;
    private final SlotService slotService;
    private final BookingService bookingService;

    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public Long save(Owner owner, CreateStoreRequest request) {
        Store store = request.toEntity(owner);

        storeRepository.save(store);
        owner.addStore(store);

        return store.getId();
    }

    @Override
    @Transactional
    public void update(Owner owner, Long storeId, UpdateStoreRequest request) {
        Store store = storeUtility.getById(storeId);
        storeUtility.validateOwner(owner, store);

        store.update(request.name(),
                request.city(),
                request.address(),
                request.contact(),
                request.description());
    }

    @Override
    public SliceResponse<StoreInfoResponse> loadAllStores(int size, Long cursorId) {
        var storeSlice = storeRepository.getAllStores(size, cursorId);

        return SliceResponse.of(storeSlice);
    }

    @Override
    public StoreInfo loadStoreDetailsById(Long id) {

        return storeRepository.getStoreDetails(id);
    }

    @Override
    public List<ScheduleInfo> loadSchedules(Long storeId) {
        Store store = storeUtility.getById(storeId);
        var schedules = store.getWeeklySchedules();

        return schedules.stream()
                .map(ScheduleInfo::of)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Owner owner, Long storeId) {
        Store store = storeUtility.getById(storeId);
        storeUtility.validateOwner(owner, store);

        bookingService.validateExistsBooking(store);

        slotService.softDeleteSlots(store.getId());
        storeRepository.delete(store);
    }
}
