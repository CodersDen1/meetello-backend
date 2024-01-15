package com.example.backend.Users;

import com.example.backend.Users.dtos.GenerateOTPRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateAndSendOTP(@RequestBody GenerateOTPRequest req){
        userService.generateAndSendOTP(req);
        return ResponseEntity.ok("OTP SENT SUCCESSFULLY");
    }

}
