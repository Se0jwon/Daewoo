package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
    @EntityGraph(attributePaths = "reviews")
    Page<AccommodationEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "reviews")
    Optional<AccommodationEntity> findById(Long id);
}
