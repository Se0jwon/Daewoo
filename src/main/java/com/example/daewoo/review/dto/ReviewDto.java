package com.example.daewoo.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private String title;
    private String content;
    private BigDecimal score;
    private Integer reviewCount;

    private Long userId;
    private String username;
    private String profileImageUrl;

    private Long comId;

    public ReviewEntity toEntity(){
        ReviewEntity entity = new ReviewEntity();
        entity.setReviewId(this.reviewId);
        entity.setTitle(this.title);
        entity.setContent(this.content);
        entity.setScore(this.score);

        return entity;
    }

    public static ReviewDto fromEntity(ReviewEntity entity){
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(entity.getReviewId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setScore(entity.getScore());
        dto.setUsername(entity.getUserEntity().getUsername());
        dto.setUserId(entity.getUserEntity().getUserId());
        dto.setComId(entity.getAccommodationEntity().getComId());
        dto.setReviewCount(entity.getAccommodationEntity().getReviewCount());
        dto.setProfileImageUrl(entity.getUserEntity().getImageUrl());
        return dto;
    }
}
