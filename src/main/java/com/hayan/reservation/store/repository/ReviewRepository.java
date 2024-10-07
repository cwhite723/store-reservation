package com.hayan.reservation.store.repository;

import com.hayan.reservation.store.domain.Review;
import com.hayan.reservation.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Modifying
    @Query("UPDATE Review r SET r.deleted = true, r.deletedAt = NOW() WHERE r.deleted = false AND r.id IN :reviewIds")
    void softDeleteReviews(List<Long> reviewIds);

    List<Review> findAllByStore(Store store);
}
