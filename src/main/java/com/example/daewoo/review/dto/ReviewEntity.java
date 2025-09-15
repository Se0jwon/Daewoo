package com.example.daewoo.review.dto;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.user.dto.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
@Builder
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String title;
    private String content;
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "com_id")
    @JsonBackReference // JSON 출력을 제한
    private AccommodationEntity accommodationEntity;
}
