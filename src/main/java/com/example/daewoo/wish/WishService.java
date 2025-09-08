package com.example.daewoo.wish;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishService {
    private final WishMapper wishMapper;

    public WishService(WishMapper wishMapper) {
        this.wishMapper = wishMapper;
    }

    public void createWish(WishDto wish) {
        wishMapper.insertWish(wish);
    }

    public WishDto getWishById(int id) {
        return wishMapper.findWishById(id);
    }

    public List<WishDto> getAllWishes() {
        return wishMapper.findAllWishes();
    }

    public void updateWish(WishDto wish) {
        wishMapper.updateWish(wish);
    }

    public void deleteWish(int id) {
        wishMapper.deleteWish(id);
    }
}