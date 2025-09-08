package com.example.daewoo.accommodation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    //    C : 숙소 추가
    @PostMapping
    public ResponseEntity<Void> createAccommodation(@RequestBody AccommodationDto accommodation) {
        accommodationService.createAccommodation(accommodation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //    R : 모든 숙소 조회
    @GetMapping
    public ResponseEntity<List<AccommodationDto>> getAllAccommodations() {
        List<AccommodationDto> accommodations = accommodationService.getAllAccommodations();
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

    //    ID에 따른 숙소 조회
    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDto> getAccommodationById(@PathVariable int id) {
        AccommodationDto accommodation = accommodationService.getAccommodationById(id);
        if (accommodation != null) {
            return new ResponseEntity<>(accommodation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //    U : 숙소 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccommodation(@PathVariable int id, @RequestBody AccommodationDto accommodation) {
        accommodation.setComId(id);
        accommodationService.updateAccommodation(accommodation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    D : 숙소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable int id) {
        accommodationService.deleteAccommodation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}