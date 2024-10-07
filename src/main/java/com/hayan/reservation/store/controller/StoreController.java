package com.hayan.reservation.store.controller;

import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.annotation.LoginCheck;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.SuccessCode;
import com.hayan.reservation.slice.SliceResponse;
import com.hayan.reservation.store.domain.dto.request.CreateStoreRequest;
import com.hayan.reservation.store.domain.dto.request.UpdateStoreRequest;
import com.hayan.reservation.store.domain.dto.response.GetStoreResponse;
import com.hayan.reservation.store.domain.dto.response.StoreInfoResponse;
import com.hayan.reservation.store.service.MenuService;
import com.hayan.reservation.store.service.ReviewService;
import com.hayan.reservation.store.service.StoreService;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MenuService menuService;
    private final ReviewService reviewService;

    @PostMapping
    @LoginCheck
    public ApplicationResponse<Long> create(@CurrentOwner Owner owner,
                                            @RequestBody CreateStoreRequest request) {
        Long storeId = storeService.save(owner, request);

        return ApplicationResponse.ok(storeId, SuccessCode.SUCCESS);
    }

    @PatchMapping("{store-id}")
    @LoginCheck
    public ApplicationResponse<Void> patch(@CurrentOwner Owner owner,
                                           @PathVariable("store-id") Long storeId,
                                           @RequestBody UpdateStoreRequest request) {
        storeService.update(owner, storeId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    @GetMapping
    public ApplicationResponse<SliceResponse<StoreInfoResponse>> getStores(
            @RequestParam int size,
            @RequestParam(defaultValue = "0") Long cursorId) {

        var stores = storeService.loadAllStores(size, cursorId);

        return ApplicationResponse.ok(stores, SuccessCode.SUCCESS);
    }

    @GetMapping("/{store-id}")
    public ApplicationResponse<GetStoreResponse> getStoreDetails(@PathVariable("store-id") Long storeId) {
        var storeDetails = storeService.loadStoreDetailsById(storeId);
        var schedules = storeService.loadSchedules(storeId);
        var menus = menuService.loadMenus(storeId);
        var reviews = reviewService.loadReviews(storeId);

        var storeAndMenus = new GetStoreResponse(storeDetails, schedules, menus, reviews);

        return ApplicationResponse.ok(storeAndMenus, SuccessCode.SUCCESS);
    }

    @LoginCheck
    @DeleteMapping("/{store-id}")
    public ApplicationResponse<Void> delete(@CurrentOwner Owner owner,
                                            @PathVariable("store-id") Long storeId) {
        storeService.delete(owner, storeId);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
