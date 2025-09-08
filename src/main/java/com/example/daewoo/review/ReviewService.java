package com.example.daewoo.review;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public void createReview(ReviewDto review) {
        reviewMapper.insertReview(review);
    }

    public ReviewDto getReviewById(long id) {
        return reviewMapper.findReviewById(id);
    }

    public List<ReviewDto> getAllReviews() {
        return reviewMapper.findAllReviews();
    }

    public void updateReview(ReviewDto review) {
        reviewMapper.updateReview(review);
    }

    public void deleteReview(long id) {
        reviewMapper.deleteReview(id);
    }
}