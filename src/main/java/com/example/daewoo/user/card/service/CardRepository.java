package com.example.daewoo.user.card.service;

import com.example.daewoo.user.card.dto.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    public List<CardEntity> findAllByUserEntity_UserId(Long userId);
}
