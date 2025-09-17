package com.example.daewoo.review.service;

import com.example.daewoo.review.dto.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT AVG(s.score) FROM ReviewEntity s WHERE s.accommodationEntity.comId = :comId")
    BigDecimal findAverageScoreByComId(@Param("comId") Long comId);
}
