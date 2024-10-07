package com.hayan.reservation.store.domain;

import com.hayan.reservation.booking.domain.DaySlot;
import com.hayan.reservation.booking.domain.SlotOption;
import com.hayan.reservation.global.BaseEntity;
import com.hayan.reservation.user.domain.Owner;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Builder
@Table(name = "stores")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE stores SET deleted = true, deletedAt = NOW() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Store extends BaseEntity {

    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private String city;

    @NonNull
    private String contact;

    private String description;

    @Column(name = "food_kind")
    private String foodKind;

    @NonNull
    @Column(name = "table_person_max")
    private Integer tablePersonMax;

    @NonNull
    @Column(name = "table_person_min")
    private Integer tablePersonMin;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "store",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private final List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "store",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private final List<WeeklySchedule> weeklySchedules = new ArrayList<>();

    @OneToOne(mappedBy = "store",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private SlotOption slotOption;

    @OneToMany(mappedBy = "store",
            cascade = CascadeType.PERSIST)
    private final List<DaySlot> daySlots = new ArrayList<>();

    @OneToMany(mappedBy = "store",
            cascade = CascadeType.PERSIST)
    private final List<Review> reviews = new ArrayList<>();

    public void update(Optional<String> name, Optional<String> city, Optional<String> address, Optional<String> contact, Optional<String> description) {
        name.ifPresent(newName -> this.name = newName);
        city.ifPresent(newCity -> this.city = newCity);
        address.ifPresent(newAddress -> this.address = newAddress);
        contact.ifPresent(newContact -> this.contact = newContact);
        description.ifPresent(newDescription -> this.description = newDescription);
    }
}
