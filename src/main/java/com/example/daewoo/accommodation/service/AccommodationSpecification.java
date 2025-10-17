package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.amenities.AmenitiesEntity;
import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccommodationSpecification {
    public static Specification<AccommodationEntity> hasPriceInRange(Integer minPrice, Integer maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }

            // ==================== 👇 [수정] "roomType"을 실제 필드명인 "rooms"로 변경 👇 ====================
            Join<AccommodationEntity, AccRoomTypeEntity> priceJoin = root.join("rooms");
            // =========================================================================================

            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(priceJoin.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(priceJoin.get("price"), maxPrice));
            }

            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // 이하 다른 메소드는 기존과 동일합니다.
    public static Specification<AccommodationEntity> hasAmenities(List<String> amCategory) {
        return (root, query, criteriaBuilder) -> {
            if (amCategory == null || amCategory.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<AccommodationEntity, AmenitiesEntity> amenitiesJoin = root.join("amenities");

            Predicate categoryInPredicate = amenitiesJoin.get("amCategory").in(amCategory);

            query.groupBy(root.get("id"));

            query.having(criteriaBuilder.equal(criteriaBuilder.countDistinct(amenitiesJoin.get("amCategory")), (long) amCategory.size()));

            return categoryInPredicate;
        };
    }

    public static Specification<AccommodationEntity> hasName(String comTitle) {
        return (root, query, criteriaBuilder) -> {
            if (comTitle == null || comTitle.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(root.get("comTitle"), "%" + comTitle + "%");
        };
    }

    public static Specification<AccommodationEntity> hasStar(Integer star) {
        return (root, query, criteriaBuilder) -> {
            if (star == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("star"), star);
        };
    }
}