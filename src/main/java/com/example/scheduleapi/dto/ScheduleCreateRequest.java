package com.example.scheduleapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreateRequest {
  private String todo;
  private Long authorId;
  private String password;
}
