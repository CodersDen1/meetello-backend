package com.example.backend.Users.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivateUserRequest {
private String id ;
private String email;
private String phoneNumber;
private String token;
private String username;
private String imageData;
}
