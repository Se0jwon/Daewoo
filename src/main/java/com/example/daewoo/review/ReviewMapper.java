package com.example.daewoo.review;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ReviewMapper {
    void insertReview(ReviewDto review);
    ReviewDto findReviewById(long id);
    List<ReviewDto> findAllReviews();
    void updateReview(ReviewDto review);
    void deleteReview(long id);
}