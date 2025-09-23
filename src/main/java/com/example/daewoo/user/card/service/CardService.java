package com.example.daewoo.user.card.service;

import com.example.daewoo.user.card.dto.CardDto;
import com.example.daewoo.user.card.dto.CardEntity;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    public void insert(CardDto dto){
        CardEntity entity = dto.toEntity();
        UserEntity userEntity = this.userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        cardRepository.save(entity);
    }

    public List<CardDto> findCardList(Long userId){
        List<CardEntity> entities = this.cardRepository.findAllByUserEntity_UserId(userId);
        return entities
                .stream()
                .map(CardDto::fromEntity)
                .toList();
    }

    public void delete(Long cardId){
        this.cardRepository.deleteById(cardId);
    }
}
