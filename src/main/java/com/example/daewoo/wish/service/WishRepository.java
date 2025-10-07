package com.example.daewoo.wish.service;

import com.example.daewoo.wish.dto.WishDto;
import com.example.daewoo.wish.dto.WishEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {
    public List<WishEntity> findByUserEntity_UserId(Long userId);
    @Query("SELECT f.accommodationEntity.comId FROM WishEntity f WHERE f.userEntity.userId = :userId AND f.accommodationEntity.comId IN :comIds")
    Set<Long> findFavoriteComIdsByUserId(@Param("userId") Long userId, @Param("comIds") List<Long> comIds);
}