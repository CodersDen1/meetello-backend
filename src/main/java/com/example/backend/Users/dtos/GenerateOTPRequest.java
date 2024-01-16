package com.example.backend.Users.dtos;


import lombok.*;

@Data
@NoArgsConstructor
public class GenerateOTPRequest {
     private String email;
     private String phoneNumber;
}
