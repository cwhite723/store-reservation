package com.hayan.reservation.slice;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CustomSliceExecutionUtils {
    public static <T> Slice<T> getSlice(List<T> content, int size) {
        boolean hasNext = false;

        if (content.size() > size) {
            content.remove(size);
            hasNext = true;
        }

        return new SliceImpl<>(content, Pageable.ofSize(size), hasNext);
    }

    public static int buildSliceLimit(int size) {
        return size + 1;
    }
}
