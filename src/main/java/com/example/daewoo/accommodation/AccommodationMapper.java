package com.example.daewoo.accommodation;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AccommodationMapper {
    //    C
    void insertAccommodation(AccommodationDto accommodation);
    //    R
    AccommodationDto findAccommodationById(int id);
    List<AccommodationDto> findAllAccommodations();
    //    U
    void updateAccommodation(AccommodationDto accommodation);
    //    D
    void deleteAccommodation(int id);
}