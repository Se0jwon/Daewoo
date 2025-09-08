package com.example.daewoo.accommodation;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccommodationService {

    private final AccommodationMapper accommodationMapper;

    public AccommodationService(AccommodationMapper accommodationMapper) {
        this.accommodationMapper = accommodationMapper;
    }

    public void createAccommodation(AccommodationDto accommodation) {
        accommodationMapper.insertAccommodation(accommodation);
    }

    public AccommodationDto getAccommodationById(int id) {
        return accommodationMapper.findAccommodationById(id);
    }

    public List<AccommodationDto> getAllAccommodations() {
        return accommodationMapper.findAllAccommodations();
    }

    public void updateAccommodation(AccommodationDto accommodation) {
        accommodationMapper.updateAccommodation(accommodation);
    }

    public void deleteAccommodation(int id) {
        accommodationMapper.deleteAccommodation(id);
    }
}