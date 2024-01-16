package com.example.backend.Users;

import com.example.backend.Users.dtos.GenerateOTPRequest;
import com.example.backend.Users.dtos.UserResponse;
import com.example.backend.Users.dtos.VerifyOtpRequest;
import com.example.backend.security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserController(UserService userService, ModelMapper modelMapper, JwtService jwtService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateAndSendOTP(@RequestBody GenerateOTPRequest req) throws NoSuchAlgorithmException, InvalidKeyException {
        userService.generateAndSendOTP(req);
        return ResponseEntity.ok("OTP SENT SUCCESSFULLY");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<UserResponse> verifyOTP(@RequestBody VerifyOtpRequest req) throws NoSuchAlgorithmException, InvalidKeyException {

        if(userService.verifyOtp(req)){
            UserEntity user = userService.getUserByEmailOrPhone(req.getEmail(),req.getPhoneNumber());
            URI userUri = URI.create("/users/"+user.getId());
            var userResponse = modelMapper.map(user, UserResponse.class);

            userResponse.setToken(
                    jwtService.createJwt(user.getId())
            );
            return ResponseEntity.created(userUri).body(userResponse);
        } else {
            throw new RuntimeException("Invalid OTP");
        }

    }

}
