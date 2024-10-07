package com.hayan.reservation.booking.service.impl;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.booking.domain.dto.request.CreateOpenRequest;
import com.hayan.reservation.booking.repository.SlotOptionsRepository;
import com.hayan.reservation.booking.service.SlotOptionService;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotOptionServiceImpl implements SlotOptionService {

    private final StoreUtility storeUtility;
    private final SlotOptionsRepository slotOptionsRepository;

    @Override
    @Transactional
    public void save(Owner owner, Long storeId, CreateOpenRequest request) {
        Store store = storeUtility.getById(storeId);
        storeUtility.validateOwner(owner, store);

        SlotOption slotOption = request.toEntity(store);
        slotOptionsRepository.save(slotOption);
    }

    @Override
    public SlotOption getByStore(Store store) {

        return slotOptionsRepository.findByStore(store)
                .orElseThrow(() -> new CustomException(ErrorCode.SLOT_OPTION_NOT_FOUND));
    }
}
