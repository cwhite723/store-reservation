package com.hayan.reservation.store.repository;

import com.hayan.reservation.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {
}
