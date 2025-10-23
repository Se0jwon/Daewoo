package com.example.daewoo.accommodation.apicontroller;

import com.example.daewoo.accommodation.accresponse.AccommodationResponse;
import com.example.daewoo.accommodation.dto.AccommodationAllDto;
import com.example.daewoo.accommodation.dto.AccommodationDiscountDto;
import com.example.daewoo.accommodation.dto.AccommodationOneDto;
import com.example.daewoo.accommodation.image.service.ComImageService;
import com.example.daewoo.accommodation.service.AccommodationService;
import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/accommodation")
public class ApiAccommodationController extends CommonRestController {

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private ComImageService comImageService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(@PageableDefault(size = 4, direction = Sort.Direction.DESC) Pageable pageable,
                                               @RequestParam(required = false) Integer minPrice,
                                               @RequestParam(required = false) Integer maxPrice,
                                               @RequestParam(defaultValue = "") List<String> amCategory,
                                               @RequestParam(defaultValue = "") String comTitle,
                                               @RequestParam(defaultValue = "") Integer star,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
                                               Authentication authentication){
        try {
            Long userId = null;

            if (authentication != null && authentication.isAuthenticated()) {
                userId = userService.findByEmail(authentication.getName()).getUserId();
            }
            if (pageable.getPageNumber() == 0) {
                Slice<AccommodationAllDto> list = this.accommodationService.findAll(minPrice, maxPrice, amCategory, comTitle, star, pageable, userId);

                Long totalCount = accommodationService.totalCount();
                AccommodationResponse res = new AccommodationResponse(totalCount, list);
                return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", res, null);
            }else{

                Slice<AccommodationAllDto> list = this.accommodationService.findAll(minPrice, maxPrice, amCategory, comTitle, star, pageable, userId);

                return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
            }
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{comId}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long comId,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut){
        try {
            AccommodationOneDto find = this.accommodationService.findById(comId, checkIn,checkOut);
            this.comImageService.findComImage(comId);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        }catch (Throwable e){
            log.error("예외 : " + e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }

    // 특가 호텔만 출력
    @GetMapping("/discount")
    public ResponseEntity<ResponseDto> findDiscountedAccommodations(@PageableDefault(size = 4, direction = Sort.Direction.DESC) Pageable pageable){
        try {
            Page<AccommodationDiscountDto> list = this.accommodationService.findDiscountedAccommodations(pageable);
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }


}