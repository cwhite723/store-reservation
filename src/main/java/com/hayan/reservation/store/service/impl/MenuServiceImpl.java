package com.hayan.reservation.store.service.impl;

import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Menu;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.dto.request.CreateMenuRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateMenuRequest;
import com.hayan.reservation.store.domain.dto.response.MenuInfo;
import com.hayan.reservation.store.repository.MenuRepository;
import com.hayan.reservation.store.service.MenuService;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final StoreUtility storeUtility;
    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public void saveAll(Owner owner, Long storeId, List<CreateMenuRequest> requests) {
        Store store = storeUtility.getById(storeId);
        storeUtility.validateOwner(owner, store);

        List<Menu> menus = requests.stream()
                .map(request -> request.toEntity(store))
                .toList();

        menuRepository.saveAll(menus);
    }

    @Override
    @Transactional
    public void update(Owner owner, Long menuId, UpdateMenuRequest request) {
        Menu menu = getById(menuId);
        storeUtility.validateOwner(owner, menu.getStore());

        menu.update(
                request.name(),
                request.price(),
                request.isPopular(),
                request.description()
        );
    }

    @Override
    public List<MenuInfo> loadMenus(Long storeId) {
        Store store = storeUtility.getById(storeId);
        List<Menu> menus = menuRepository.findAllByStore(store);

        return menus.stream()
                .map(menu -> new MenuInfo(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.isPopular(),
                        menu.getDescription()
                ))
                .toList();
    }

    private Menu getById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }
}
