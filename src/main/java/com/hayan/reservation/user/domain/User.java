package com.hayan.reservation.user.domain;

import com.hayan.reservation.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @NonNull
    private String name;

    @NonNull
    private String contact;

    @Column(unique = true)
    private String username;

    @NonNull
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    public User(String name, String contact, String username, String password, Set<String> roles) {
        this.name = name;
        this.contact = contact;
        this.username = username;
        this.password = password;
        this.roles.addAll(roles);
    }
}