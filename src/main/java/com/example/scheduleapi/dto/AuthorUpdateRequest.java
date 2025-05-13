package com.example.scheduleapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorUpdateRequest {
    private String name;
    private String email;
}