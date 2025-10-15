package com.example.daewoo.accommodation.dto;

import com.example.daewoo.accommodation.amenities.AmenitiesEntity;
import com.example.daewoo.accommodation.location.dto.LocationEntity;
import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import com.example.daewoo.reservation.dto.ReservationEntity;
import com.example.daewoo.review.dto.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accommodation")
@Builder
public class AccommodationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comId;

    private String comTitle;
    private String comDescription;
    private String comAddress;
    private BigDecimal reviewAvg;
    private Integer reviewCount;
    private Integer star;
    private String category;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity locationEntity;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "accommodation_amenity", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "com_id"), // 내 컬럼
            inverseJoinColumns = @JoinColumn(name = "am_id") // 상대 컬럼
    )
    private List<AmenitiesEntity> amenities = new ArrayList<>();

//    @OneToMany(mappedBy = "accommodationEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference // JSON 출력
//    private List<ReviewEntity> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "accommodation")
    private List<AccRoomTypeEntity> rooms = new ArrayList<>();
}
