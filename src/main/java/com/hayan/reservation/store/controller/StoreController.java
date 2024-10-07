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

    /*

    매장 등록 api
    POST http://localhost:8080/api/store

    {
        "name" : "하얀이네 식당",
        "city" : "강서구",
        "address" : "서울시 강서구 어쩌고 저쩌고",
        "contact" : "02-1111-2222",
        "description" : "맛있어요~~",
        "foodKind" : "양식",
        "tablePersonMax" : 10,
        "tablePersonMin" : 1
    }

     */
    @PostMapping
    @LoginCheck
    public ApplicationResponse<Long> create(@CurrentOwner Owner owner,
                                            @RequestBody CreateStoreRequest request) {
        Long storeId = storeService.save(owner, request);

        return ApplicationResponse.ok(storeId, SuccessCode.SUCCESS);
    }


    /*
    매장 수정 api
    PATCH http://localhost:8080/api/store/1

    {
        "name" : "하얀이네 식당",
        "city" : "강서구",
        "address" : "서울시 강서구 어쩌고 저쩌고",
        "contact" : "02-1111-2222",
        "description" : "맛있어요~~"
    }

     */
    @PatchMapping("{store-id}")
    @LoginCheck
    public ApplicationResponse<Void> patch(@CurrentOwner Owner owner,
                                           @PathVariable("store-id") Long storeId,
                                           @RequestBody UpdateStoreRequest request) {
        storeService.update(owner, storeId, request);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }

    /*

    매장 리스트 조회 api
    GET http://localhost:8080/api/store?size=3&cursorId=0

     */
    @GetMapping
    public ApplicationResponse<SliceResponse<StoreInfoResponse>> getStores(
            @RequestParam int size,
            @RequestParam(defaultValue = "0") Long cursorId) {

        var stores = storeService.loadAllStores(size, cursorId);

        return ApplicationResponse.ok(stores, SuccessCode.SUCCESS);
    }

    /*

    매장 상세 조회 api
    GET http://localhost:8080/api/store/1

     */
    @GetMapping("/{store-id}")
    public ApplicationResponse<GetStoreResponse> getStoreDetails(@PathVariable("store-id") Long storeId) {
        var storeDetails = storeService.loadStoreDetailsById(storeId);
        var schedules = storeService.loadSchedules(storeId);
        var menus = menuService.loadMenus(storeId);
        var reviews = reviewService.loadReviews(storeId);

        var storeAndMenus = new GetStoreResponse(storeDetails, schedules, menus, reviews);

        return ApplicationResponse.ok(storeAndMenus, SuccessCode.SUCCESS);
    }

    /*

    매장 삭제 api
    DELETE http://localhost:8080/api/store/1

     */
    @LoginCheck
    @DeleteMapping("/{store-id}")
    public ApplicationResponse<Void> delete(@CurrentOwner Owner owner,
                                            @PathVariable("store-id") Long storeId) {
        storeService.delete(owner, storeId);

        return ApplicationResponse.noData(SuccessCode.SUCCESS);
    }
}
