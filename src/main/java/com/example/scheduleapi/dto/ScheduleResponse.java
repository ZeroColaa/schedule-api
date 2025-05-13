package com.example.scheduleapi.dto;


import com.example.scheduleapi.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ScheduleResponse {

    private Long id;
    private String todo;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static ScheduleResponse from(Schedule s,String authorName){

        return new ScheduleResponse(
                s.getId(),
                s.getTodo(),
                authorName,
                s.getCreatedAt(),
                s.getModifiedAt()
        );

    }







}
