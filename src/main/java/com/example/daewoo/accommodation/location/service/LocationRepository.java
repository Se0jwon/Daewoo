package com.example.daewoo.accommodation.location.service;

import com.example.daewoo.accommodation.location.dto.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
}
