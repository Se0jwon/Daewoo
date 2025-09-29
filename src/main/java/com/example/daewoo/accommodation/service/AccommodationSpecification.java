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
            // 가격 조건이 없으면 아무것도 하지 않음
            if (minPrice == null && maxPrice == null) {
                return null;
            }

            // Hotel(root)에서 "prices" 필드를 기준으로 Price 엔티티와 조인
            Join<AccommodationEntity, AccRoomTypeEntity> priceJoin = root.join("room");

            List<Predicate> predicates = new ArrayList<>();

            // 최소 가격 조건 추가
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(priceJoin.get("price"), minPrice));
            }

            // 최대 가격 조건 추가
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(priceJoin.get("price"), maxPrice));
            }

            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<AccommodationEntity> hasAmenities(List<String> amCategory) {
        return (root, query, criteriaBuilder) -> {
            // 1. 편의시설 목록이 비어있으면 아무 조건도 적용하지 않음
            if (amCategory == null || amCategory.isEmpty()) {
                return criteriaBuilder.conjunction(); // new Predicate[0] 와 동일, 항상 true
            }

            // 2. AccommodationEntity와 AmenitiesEntity를 조인
            Join<AccommodationEntity, AmenitiesEntity> amenitiesJoin = root.join("amenities");

            // 3. 조인된 편의시설의 카테고리가 요청된 목록(amCategories)에 포함되는지(IN) 확인
            Predicate categoryInPredicate = amenitiesJoin.get("amCategory").in(amCategory);

            // 4. GROUP BY와 HAVING 절을 적용하기 위해 쿼리를 수정
            // 숙소(root)를 기준으로 그룹화
            query.groupBy(root.get("id"));

            // 그룹화된 결과에 대해, 조인된 편의시설의 개수가 요청된 편의시설의 개수와 같은지 확인
            // COUNT(amenitiesJoin)는 CriteriaBuilder의 count 메서드가 Long을 반환하므로 (long)으로 캐스팅
            query.having(criteriaBuilder.equal(criteriaBuilder.count(amenitiesJoin), (long) amCategory.size()));

            // 최종적으로 WHERE 절에 IN 조건을 적용하여 반환
            return categoryInPredicate;
        };
    }

    public static Specification<AccommodationEntity> hasName(String comTitle) {
        return (root, query, criteriaBuilder) -> {
            // name 파라미터가 비어있으면 검색 조건을 적용하지 않음
            if (comTitle == null || comTitle.isBlank()) {
                return null;
            }
            // "name" 필드에서 부분 일치(like) 검색
            return criteriaBuilder.like(root.get("comTitle"), "%" + comTitle + "%");
        };
    }

    public static Specification<AccommodationEntity> hasStar(Integer star) {
        return (root, query, criteriaBuilder) -> {
            // name 파라미터가 비어있으면 검색 조건을 적용하지 않음
            if (star == null) {
                return null;
            }
            // "name" 필드에서 부분 일치(like) 검색
            return criteriaBuilder.equal(root.get("star"), star);
        };
    }
}
