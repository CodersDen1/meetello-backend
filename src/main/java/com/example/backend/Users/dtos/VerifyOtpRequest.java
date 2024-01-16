package com.example.backend.Users.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class VerifyOtpRequest {
    private String email;
    private String phoneNumber;
    private String otp;
}
