package com.hayan.reservation.store.controller;

import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.annotation.LoginCheck;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.store.domain.dto.request.CreateMenuRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateMenuRequest;
import com.hayan.reservation.store.service.MenuService;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{store-id}")
    @LoginCheck
    public ApplicationResponse<Void> createAll(@CurrentOwner Owner owner,
                                               @PathVariable("store-id") Long storeId,
                                               @RequestBody List<CreateMenuRequest> requests) {
        menuService.saveAll(owner, storeId, requests);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @PatchMapping("/{menu-id}")
    public ApplicationResponse<Void> patch(@CurrentOwner Owner owner,
                                           @PathVariable("menu-id") Long menuId,
                                           @RequestBody UpdateMenuRequest request) {
        menuService.update(owner, menuId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
