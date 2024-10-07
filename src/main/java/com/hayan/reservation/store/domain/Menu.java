package com.hayan.reservation.store.domain;

import com.hayan.reservation.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Optional;

@Getter
@Entity
@Builder
@DynamicUpdate
@Table(name = "menus")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @NonNull
    private String name;

    @NonNull
    private Integer price;

    private boolean isPopular;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void update(Optional<String> name, Optional<Integer> price, Optional<Boolean> isPopular, Optional<String> description) {
        name.ifPresent(newName -> this.name = newName);
        price.ifPresent(newPrice -> this.price = newPrice);
        isPopular.ifPresent(newIsPopular -> this.isPopular = newIsPopular);
        description.ifPresent(newDescription -> this.description = newDescription);
    }
}
