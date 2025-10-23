package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationDiscountDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long>, JpaSpecificationExecutor<AccommodationEntity> {
//    @EntityGraph(attributePaths = "reviews")
//    Page<AccommodationEntity> findAll(Pageable pageable);

    @Query("SELECT COUNT(s) FROM AccommodationEntity s")
    Long hotelCount();

    @Query("SELECT MIN(r.price) FROM AccRoomTypeEntity r WHERE r.accommodation.comId = :comId")
    Integer findLowestPriceByHotelId(@Param("comId") Long comId);

    @Query("SELECT c.imageUrl FROM ComImageEntity c WHERE c.accommodation.comId = :comId AND c.isMain = true")
    String findMainComImage(@Param("comId") Long comId);

    @Query("SELECT c.imageUrl FROM ComImageEntity c WHERE c.accommodation.comId = :comId AND c.isMain = false")
    List<String> findSubComImage(@Param("comId") Long comId);



    @Query(
            "SELECT NEW com.example.daewoo.accommodation.dto.AccommodationDiscountDto(" +
                    "   a.comId, " +
                    "   a.comTitle, " +
                    "   (SELECT img.imageUrl FROM ComImageEntity img WHERE img.accommodation = a AND img.isMain = true), " +
                    "   (SELECT MIN(art.price) FROM AccRoomTypeEntity art WHERE art.accommodation = a), " +
                    "   a.discountRate," +
                    "   a.locationEntity.locationName" +
                    ") " +
                    "FROM AccommodationEntity a " +
                    "WHERE a.discountRate > :rate"
    )
    Page<AccommodationDiscountDto> findDiscountedHotels(@Param("rate") BigDecimal rate, Pageable pageable);

    Long comId(Long comId);

    @Query("SELECT CAST(COUNT(am) AS Integer) " +
            "FROM AccommodationEntity a " +
            "JOIN a.amenities am " +
            "WHERE a.comId = :comId")
    Integer countAmenitiesByAccommodationId(@Param("comId") Long comId);

//    @EntityGraph(attributePaths = "reviews")
//    Optional<AccommodationEntity> findById(Long id);
}
