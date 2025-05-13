package com.example.scheduleapi.controller;


import com.example.scheduleapi.dto.*;
import com.example.scheduleapi.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService service;


    //1.일정 생성
    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@RequestBody ScheduleCreateRequest request){
        ScheduleResponse response = service.create(request);
        return ResponseEntity.status(201).body(response);
    }

    // 2. 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAll(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String modifiedDate) {
        return ResponseEntity.ok(service.findAll(author, modifiedDate));
    }

    //3.선택 일정 조회

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    //4.선택한 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> update(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRequest req
    ){
        return ResponseEntity.ok(service.update(id, req));
    }

    //5.선택한 일정 삭제

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestBody ScheduleDeleteRequest req
    ){
        service.delete(id,req);
        return ResponseEntity.noContent().build(); //204
    }



}
