package com.example.daewoo.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {
    private Long wishId;
    private Long userId;
    private Long parlorId;

    public WishEntity toEntity(){
        WishEntity entity = new WishEntity();
        entity.setWishId(this.wishId);
        return entity;
    }

    public static WishDto fromEntity(WishEntity entity){
        WishDto dto = new WishDto();
        dto.setWishId(entity.getWishId());
        dto.setUserId(entity.getUserEntity().getUserId());
        dto.setParlorId(entity.getParlorEntity().getParId());

        return dto;
    }
}