package com.example.backend.Users.dtos;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String email;
    private String password;
}