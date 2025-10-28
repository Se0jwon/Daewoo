package com.example.daewoo.wish.service;

import com.example.daewoo.accommodation.dto.AccommodationAllDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.accommodation.service.AccommodationRepository;
import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.service.ParlorRepository;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import com.example.daewoo.wish.dto.WishDto;
import com.example.daewoo.wish.dto.WishEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WishService {
    @Autowired
    private WishRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    public WishDto insert(Long userId, WishDto dto) {
        WishEntity entity = dto.toEntity();

        AccommodationEntity accommodationEntity = accommodationRepository.findById(dto.getAccommodationAllDto().getComId())
                .orElseThrow(() -> new RuntimeException("Parlor Not Found"));
        entity.setAccommodationEntity(accommodationEntity);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);
        this.repository.save(entity);

        return dto;
    }

    public List<WishDto> findAll(Long userId) {

        return this.repository.findByUserEntity_UserId(userId).stream()
                .map(wishEntity -> {
                    WishDto wishDto = WishDto.fromEntity(wishEntity);
                    AccommodationAllDto accommodationDto = wishDto.getAccommodationAllDto();

                    if (accommodationDto != null) {
                        Long comId = accommodationDto.getComId();

                        Integer price = accommodationRepository.findLowestPriceByHotelId(comId);
                        String image = accommodationRepository.findMainComImage(comId);
                        Integer amenitiesCount = accommodationRepository.countAmenitiesByAccommodationId(comId);

                        accommodationDto.setPrice(price);
                        accommodationDto.setImage(image);
                        accommodationDto.setAmenitiesCount(amenitiesCount);
                        accommodationDto.setIsFavorite(Boolean.TRUE);
                    }

                    return wishDto;
                })
                .collect(Collectors.toList());
    }


    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}