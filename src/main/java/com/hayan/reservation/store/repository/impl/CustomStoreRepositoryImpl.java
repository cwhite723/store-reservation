package com.hayan.reservation.store.repository.impl;

import com.hayan.reservation.slice.CustomSliceExecutionUtils;
import com.hayan.reservation.store.domain.QStore;
import com.hayan.reservation.store.domain.dto.response.QStoreInfo;
import com.hayan.reservation.store.domain.dto.response.QStoreInfoResponse;
import com.hayan.reservation.store.domain.dto.response.StoreInfo;
import com.hayan.reservation.store.domain.dto.response.StoreInfoResponse;
import com.hayan.reservation.store.repository.CustomStoreRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import static com.hayan.reservation.store.domain.QReview.review;
import static com.hayan.reservation.store.domain.QStore.store;

@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<StoreInfoResponse> getAllStores(int size, Long cursorId) {
        JPQLQuery<StoreInfoResponse> query = queryFactory.select(new QStoreInfoResponse(
                        store.id,
                        store.name,
                        store.city,
                        store.address,
                        store.contact,
                        store.description
                ))
                .from(store)
                .where(gtCursorId(cursorId))
                .orderBy(store.id.asc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(size));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), size);
    }

    @Override
    public StoreInfo getStoreDetails(Long id) {
        return queryFactory.select(new QStoreInfo(
                        store.id,
                        store.name,
                        store.contact,
                        store.city,
                        store.address,
                        store.description,
                        store.reviews.size(),
                        calculateStarPointExpression(store),
                        store.owner.id,
                        store.owner.name
                ))
                .from(store)
                .where(store.id.eq(id))
                .fetchOne();
    }

    private Expression<Double> calculateStarPointExpression(QStore store) {
        return queryFactory
                .select(review.starPoint.avg())
                .from(review)
                .where(review.store.eq(store));
    }

    private BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;

        return store.id.gt(cursorId);
    }
}
