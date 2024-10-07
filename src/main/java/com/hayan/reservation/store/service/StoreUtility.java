package com.hayan.reservation.store.service;

import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.repository.StoreRepository;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreUtility {

    private final StoreRepository storeRepository;

    public void validateOwner(Owner owner, Store store) {

        if (!Objects.equals(owner, store.getOwner()))
            throw new CustomException(ErrorCode.NOT_OWNER);
    }

    public Store getById(Long id) {

        return storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
