package com.example.daewoo.parlor.service;

import com.example.daewoo.parlor.dto.ParlorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParlorRepository extends JpaRepository<ParlorEntity, Long> {
}
