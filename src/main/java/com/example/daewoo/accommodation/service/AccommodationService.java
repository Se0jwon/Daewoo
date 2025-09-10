package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.review.dto.ReviewEntity;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccommodationService {
    @Autowired
    private AccommodationRepository accommodationRepository;



    public void insert(AccommodationDto dto) {
        AccommodationEntity entity = dto.toEntity();

//        UserEntity userEntity = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User Not Found"));
//        entity.setUserEntity(userEntity);

        this.accommodationRepository.save(entity);
    }

    public Page<ReviewDto> findAll(Pageable pageable){
        Page<ReviewEntity> entities = accommodationRepository.findAll(pageable);

        return entities.map(ReviewDto::fromEntity);
    }

    public Optional<ReviewDto> findById(Long id){
        return accommodationRepository.findById(id)
                .map(ReviewDto::fromEntity);
    }

    public void update(ReviewDto dto){
        ReviewEntity entity = dto.toEntity();

        UserEntity userEntity = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        this.accommodationRepository.save(entity);
    }

    public void delete(Long id){
        this.accommodationRepository.deleteById(id);
    }
}
