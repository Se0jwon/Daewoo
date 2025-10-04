package com.example.daewoo.parlor.service;

import com.example.daewoo.accommodation.service.AccommodationRepository;
import com.example.daewoo.parlor.dto.ParlorDto;
import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.roomtype.AccRoomTypeDto;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import com.example.daewoo.user.dto.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParlorService {
    @Autowired
    private ParlorRepository parlorRepository;

    @Autowired
    private AccRoomTypeRepository accRoomTypeRepository;


    public void insert(ParlorDto dto){
        ParlorEntity entity = dto.toEntity();

        AccRoomTypeEntity accRoomType = accRoomTypeRepository.findById(dto.getAccId())
                .orElseThrow(() -> new RuntimeException("RoomType Not Found"));
        entity.setAccRoomTypeEntity(accRoomType);

        this.parlorRepository.save(entity);

        AccRoomTypeEntity accRoomTypeEntity = accRoomTypeRepository.findById(dto.getAccId()).orElseThrow();
        Integer maxRoom = this.parlorRepository.countMaxRoomByParlor(dto.getAccId());
        accRoomTypeEntity.setMaxRoom(maxRoom);
        accRoomTypeRepository.save(accRoomTypeEntity);
    }
}
