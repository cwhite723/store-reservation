package com.hayan.reservation.store.domain.dto.request;

import java.util.Optional;

public record UpdateStoreRequest(Optional<String> name,
                                 Optional<String> city,
                                 Optional<String> address,
                                 Optional<String> contact,
                                 Optional<String> description) {
}
