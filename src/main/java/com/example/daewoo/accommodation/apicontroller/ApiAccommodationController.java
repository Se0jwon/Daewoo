package com.example.daewoo.accommodation.apicontroller;

import com.example.daewoo.accommodation.accommodation.AmenitiesRepository;
import com.example.daewoo.accommodation.dto.AccommodationDto;
import com.example.daewoo.accommodation.service.AccommodationService;
import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/accommodation")
public class ApiAccommodationController extends CommonRestController {

    @Autowired
    private AccommodationService accommodationService;

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(@PageableDefault(size = 5, sort = "comId", direction = Sort.Direction.ASC)
                                                         Pageable pageable){
        try {
            Page<AccommodationDto> list = this.accommodationService.findAll(pageable);
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long id){
        try {
            Optional<AccommodationDto> find = this.accommodationService.findById(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }

}
