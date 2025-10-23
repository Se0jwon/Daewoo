package com.example.daewoo.parlor.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.parlor.dto.ParlorDto;
import com.example.daewoo.parlor.roomtype.AccRoomTypeDto;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import com.example.daewoo.parlor.roomtype.PaymentAccRoomTypeDto;
import com.example.daewoo.parlor.service.ParlorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("api/room")
public class ApiParlorController extends CommonRestController {

    @Autowired
    private ParlorService parlorService;

    @PostMapping("")
    public ResponseEntity<ResponseDto> maxRoomCount(@RequestBody ParlorDto dto){
        try{
            parlorService.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", null, e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long id,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut){
        try{
            PaymentAccRoomTypeDto dto = parlorService.findById(id);
            dto.setCheckIn(checkIn);
            dto.setCheckOut(checkOut);
            return getResponseEntity(ResponseCode.SUCCESS, "Select Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Select Error", null, e);
        }

    }
}
