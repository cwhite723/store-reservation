package com.hayan.reservation.store.service;

import com.hayan.reservation.store.domain.dto.request.CreateMenuRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateMenuRequest;
import com.hayan.reservation.store.domain.dto.response.MenuInfo;
import com.hayan.reservation.user.domain.Owner;

import java.util.List;

public interface MenuService {
    void saveAll(Owner owner, Long storeId, List<CreateMenuRequest> requests);
    void update(Owner owner, Long menuId, UpdateMenuRequest request);
    List<MenuInfo> loadMenus(Long storeId);
}
