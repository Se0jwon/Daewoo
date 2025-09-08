package com.example.daewoo.wish;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

//    찜 등록
    @PostMapping
    public ResponseEntity<Void> createWish(@RequestBody WishDto wish) {
        wishService.createWish(wish);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    찜 전체조회
    @GetMapping
    public ResponseEntity<List<WishDto>> getAllWishes() {
        List<WishDto> wishes = wishService.getAllWishes();
        return new ResponseEntity<>(wishes, HttpStatus.OK);
    }

//    찜 개별조회
    @GetMapping("/{id}")
    public ResponseEntity<WishDto> getWishById(@PathVariable int id) {
        WishDto wish = wishService.getWishById(id);
        if (wish != null) {
            return new ResponseEntity<>(wish, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    찜 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWish(@PathVariable int id, @RequestBody WishDto wish) {
        wish.setWishId(id);
        wishService.updateWish(wish);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    찜 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable int id) {
        wishService.deleteWish(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}