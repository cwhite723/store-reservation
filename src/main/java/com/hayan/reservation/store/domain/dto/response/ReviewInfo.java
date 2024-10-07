package com.hayan.reservation.store.domain.dto.response;

import com.hayan.reservation.store.domain.Review;
import lombok.Builder;

@Builder
public record ReviewInfo(Long reviewId,
                         String content,
                         int starPoint,
                         Long userId,
                         String username) {

    public static ReviewInfo of(Review review) {

        return ReviewInfo.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .starPoint(review.getStarPoint())
                .userId(review.getCustomer().getId())
                .username(review.getCustomer().getUsername())
                .build();
    }
}
