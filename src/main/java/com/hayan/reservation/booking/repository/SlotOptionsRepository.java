package com.hayan.reservation.booking.repository;

import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotOptionsRepository extends JpaRepository<SlotOption, Long> {
    Optional<SlotOption> findByStore(Store store);
}
