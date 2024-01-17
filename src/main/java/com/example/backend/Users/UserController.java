package com.example.backend.Users;

import com.example.backend.Users.dtos.GenerateOTPRequest;
import com.example.backend.Users.dtos.UserResponse;
import com.example.backend.Users.dtos.VerifyOtpRequest;
import com.example.backend.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpResponse;
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
    public ResponseEntity<UserResponse> generateAndSendOTP(@RequestBody GenerateOTPRequest req) throws NoSuchAlgorithmException, InvalidKeyException {
        userService.generateAndSendOTP(req);
        UserEntity user = userService.getUserByEmailOrPhone(req.getEmail(), req.getPhoneNumber());
        var resposne = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(resposne);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<UserResponse> verifyOTP(@RequestBody VerifyOtpRequest req , HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeyException {

        if(userService.verifyOtp(req)){
            UserEntity user = userService.getUserByEmailOrPhone(req.getEmail(),req.getPhoneNumber());
            URI userUri = URI.create("/users/"+user.getId());
            var userResponse = modelMapper.map(user, UserResponse.class);
            String jwtToken = jwtService.createJwt(user.getId());
            String refreshToken = jwtService.createRefreshJWT(user.getId());


            //jwtCOokie
            Cookie jwtCookie = new Cookie("jwtToken",jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(60*15);
            jwtCookie.setPath("/");

            //refresh jwt cookie
            Cookie refreshJwtCookie = new Cookie("refresh-token" , refreshToken);
            refreshJwtCookie.setHttpOnly(true);
            refreshJwtCookie.setMaxAge(60*15);
            refreshJwtCookie.setPath("/");

            response.addCookie(jwtCookie);

            userResponse.setToken(jwtToken);

            return ResponseEntity.created(userUri).body(userResponse);
        } else {
            throw new RuntimeException("Invalid OTP");
        }

    }

}
