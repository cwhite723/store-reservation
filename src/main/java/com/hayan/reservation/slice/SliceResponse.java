package com.hayan.reservation.slice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Builder
public class SliceResponse<T> {
    private boolean isLast;
    private List<T> content;

    public static <T> SliceResponse<T> of(Slice<T> slice) {
        return SliceResponse.<T>builder()
                .isLast(slice.isLast())
                .content(slice.getContent())
                .build();
    }
}