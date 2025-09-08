package com.example.daewoo.wish;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WishMapper {
    void insertWish(WishDto wish);
    WishDto findWishById(int id);
    List<WishDto> findAllWishes();
    void updateWish(WishDto wish);
    void deleteWish(int id);
}