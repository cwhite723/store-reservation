package com.hayan.reservation.user.domain;

import com.hayan.reservation.store.domain.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@DiscriminatorValue("owner")
@NoArgsConstructor
public class Owner extends User {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Store> stores = new ArrayList<>();

    @Builder
    public Owner(String name, String contact, String username, String password, Set<String> roles) {
        super(name, contact, username, password, roles);
    }

    public void addStore(Store store) {
        stores.add(store);
    }
}
