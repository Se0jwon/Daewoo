package com.example.daewoo.accommodation.apicontroller;

import com.example.daewoo.accommodation.accresponse.AccommodationResponse;
import com.example.daewoo.accommodation.dto.AccommodationAllDto;
import com.example.daewoo.accommodation.dto.AccommodationOneDto;
import com.example.daewoo.accommodation.service.AccommodationService;
import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/accommodation")
public class ApiAccommodationController extends CommonRestController {

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(@PageableDefault(size = 4, direction = Sort.Direction.DESC) Pageable pageable,
                                               @RequestParam(defaultValue = "") Integer minPrice, @RequestParam(defaultValue = "") Integer maxPrice,
                                               @RequestParam(defaultValue = "") List<String> amCategory,
                                               @RequestParam(defaultValue = "") String comTitle,
                                               @RequestParam(defaultValue = "") Integer star){
        try {
            if (pageable.getPageNumber() == 0) {
                Slice<AccommodationAllDto> list = this.accommodationService.findAll(minPrice, maxPrice, amCategory, comTitle, star, pageable);
                Long totalCount = accommodationService.totalCount();
                AccommodationResponse res = new AccommodationResponse(totalCount, list);
                return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", res, null);
            }else{
                Slice<AccommodationAllDto> list = this.accommodationService.findAll(minPrice, maxPrice, amCategory, comTitle, star, pageable);
                return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
            }
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{comId}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long comId){
        try {
            Optional<AccommodationOneDto> find = this.accommodationService.findById(comId);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        }catch (Throwable e){
            log.error("예외 : " + e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }


    @GetMapping("/{comId}/review")
    public ResponseEntity<ResponseDto> findReview(@PathVariable Long comId,
                                                  @PageableDefault(size = 5, sort = "reviewId", direction = Sort.Direction.DESC)
                                                  Pageable pageable){
        try {
            Page<ReviewDto> list = this.reviewService.findAllByAccommodationEntity_ComId(comId, pageable);
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }
}
