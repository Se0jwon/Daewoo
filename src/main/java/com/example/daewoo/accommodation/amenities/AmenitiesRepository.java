package com.example.daewoo.accommodation.amenities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AmenitiesRepository extends JpaRepository<AmenitiesEntity, Long> {
}
