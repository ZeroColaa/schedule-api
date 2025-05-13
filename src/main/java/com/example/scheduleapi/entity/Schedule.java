package com.example.scheduleapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {


    private Long id;
    private String todo;
    private Long authorId;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;



}
