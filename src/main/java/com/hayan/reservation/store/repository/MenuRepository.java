package com.hayan.reservation.store.repository;

import com.hayan.reservation.store.domain.Menu;
import com.hayan.reservation.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStore(Store store);
}
