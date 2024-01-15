package com.example.backend.Users.dtos;


import lombok.*;

@Data
@NoArgsConstructor
public class GenerateOTPRequest {
    @NonNull private String userAddress;
}
