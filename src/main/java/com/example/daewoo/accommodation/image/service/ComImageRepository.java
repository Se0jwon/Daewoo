package com.example.daewoo.accommodation.image.service;

import com.example.daewoo.accommodation.image.dto.ComImageDto;
import com.example.daewoo.accommodation.image.dto.ComImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ComImageRepository extends JpaRepository<ComImageEntity, Long> {
    public List<ComImageEntity> findByAccommodation_ComId(Long comId);
}
