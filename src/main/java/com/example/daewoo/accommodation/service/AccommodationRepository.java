package com.example.daewoo.accommodation.service;

import com.example.daewoo.review.dto.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<ReviewEntity, Long> {
}
