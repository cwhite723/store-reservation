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

    /*

    메뉴 리스트를 등록하는 api
    POST http://localhost:8080/api/menu/1

    [
        {
            "name" : "샤브샤브",
            "price" : 15000,
            "isPopular" : true,
            "description" : "소고기 샤브샤브입니다 ~!"
        },
        {
            "name" : "월남쌈",
            "price" : 11000,
            "isPopular" : false,
            "description" : "샤브샤브랑 함께 먹으면 마싯서용"
        }
    ]

     */
    @PostMapping("/{store-id}")
    @LoginCheck
    public ApplicationResponse<Void> createAll(@CurrentOwner Owner owner,
                                               @PathVariable("store-id") Long storeId,
                                               @RequestBody List<CreateMenuRequest> requests) {
        menuService.saveAll(owner, storeId, requests);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    메뉴를 단건으로 수정하는 api (이름, 가격 등 Menu의 필드 중 원하는 값만 넣어서 수정)
    PATCH  http://localhost:8080/api/menu/1

    {
        "price" : 3000
    }

     */
    @PatchMapping("/{menu-id}")
    public ApplicationResponse<Void> patch(@CurrentOwner Owner owner,
                                           @PathVariable("menu-id") Long menuId,
                                           @RequestBody UpdateMenuRequest request) {
        menuService.update(owner, menuId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
