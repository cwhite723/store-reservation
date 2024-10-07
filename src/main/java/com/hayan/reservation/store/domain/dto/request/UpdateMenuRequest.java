package com.hayan.reservation.store.domain.dto.request;

import java.util.Optional;

public record UpdateMenuRequest(Optional<String> name,
                                Optional<Integer> price,
                                Optional<Boolean> isPopular,
                                Optional<String> description) {
}
