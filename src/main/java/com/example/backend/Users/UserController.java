package com.example.backend.Users;

import com.example.backend.Users.dtos.GenerateOTPRequest;
import com.example.backend.Users.dtos.UserResponse;
import com.example.backend.Users.dtos.VerifyOtpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateAndSendOTP(@RequestBody GenerateOTPRequest req) throws NoSuchAlgorithmException, InvalidKeyException {
        userService.generateAndSendOTP(req);
        return ResponseEntity.ok("OTP SENT SUCCESSFULLY");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOtpRequest req) throws NoSuchAlgorithmException, InvalidKeyException {

        if(userService.verifyOtp(req)){
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

    }

}
