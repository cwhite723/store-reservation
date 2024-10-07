package com.hayan.reservation.store.service.impl;

import com.hayan.reservation.booking.domain.Booking;
import com.hayan.reservation.booking.service.BookingService;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.store.domain.Review;
import com.hayan.reservation.store.domain.Store;
import com.hayan.reservation.store.domain.dto.request.ReviewRequest;
import com.hayan.reservation.store.domain.dto.response.ReviewInfo;
import com.hayan.reservation.store.repository.ReviewRepository;
import com.hayan.reservation.store.service.ReviewService;
import com.hayan.reservation.store.service.StoreUtility;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final BookingService bookingService;
    private final StoreUtility storeUtility;

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Long save(Customer customer, Long bookingId, ReviewRequest request) {
        Booking booking = bookingService.getById(bookingId);
        Store store = bookingService.getStoreByBooking(booking);
        bookingService.validateCustomer(customer, booking.getCustomer());
        booking.isVisited();

        Review review = request.toEntity(store, booking, customer);
        reviewRepository.save(review);

        return review.getId();
    }

    @Override
    @Transactional
    public void update(Customer customer, Long reviewId, ReviewRequest request) {
        Review review = getById(reviewId);
        validateAuthor(customer, review.getCustomer());

        review.update(review.getContent(), review.getStarPoint());
    }

    @Override
    @Transactional
    public void deleteByAuthor(Customer customer, Long reviewId) {
        Review review = getById(reviewId);
        validateAuthor(customer, review.getCustomer());

        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public void deleteByOwner(Owner owner, Long reviewId, String reason) {
        Review review = getById(owner.getId());
        validateOwner(owner, review.getStore().getOwner());

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewInfo> loadReviews(Long storeId) {
        Store store = storeUtility.getById(storeId);

        return reviewRepository.findAllByStore(store).stream()
                .map(ReviewInfo::of)
                .toList();
    }

    private Review getById(Long id) {

        return reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private void validateAuthor(Customer loginCustomer, Customer author) {

        if (!Objects.equals(loginCustomer, author))
            throw new CustomException(ErrorCode.NOT_AUTHOR);
    }

    private void validateOwner(Owner loginOwner, Owner owner) {

        if (!Objects.equals(loginOwner, owner))
            throw new CustomException(ErrorCode.NOT_OWNER);
    }
}
