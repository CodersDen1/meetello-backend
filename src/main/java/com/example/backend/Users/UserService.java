package com.example.backend.Users;

import com.example.backend.Users.dtos.GenerateOTPRequest;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final  SmsService smsService;


    public UserService(UserRepository userRepository , ModelMapper modelMapper, JavaMailSender javaMailSender, SmsService smsService) {
        this.modelMapper= modelMapper;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.smsService = smsService;
    }

    public void generateAndSendOTP(GenerateOTPRequest req){
        String otp= generateOTP();

        UserEntity user = userRepository.existsByUserAddress(req.getUserAddress())
                ? userRepository.findByUserAddress(req.getUserAddress())
                    : modelMapper.map(req , UserEntity.class);

        user.setOtp(otp);
        userRepository.save(user);
        if (user.getUserAddress().contains("@")) {
            sendEmailOTP(user.getUserAddress(), " Hello this is akki sending you  Your OTP: \t " + otp +"\t from backend ", "OTP Confirmation");
        } else {
            sendMobileOTP(user.getUserAddress());
        }
        ;
    }



    public void sendMobileOTP(String userAddress){
            String otp = generateOTP();

        UserEntity user = userRepository.existsByUserAddress(userAddress)
                ? userRepository.findByUserAddress(userAddress)
                : modelMapper.map( userAddress , UserEntity.class);
            user.setOtp(otp);
            userRepository.save(user);

            smsService.sendOtpSms(user.getUserAddress(),otp);

    }

    public void sendEmailOTP(String toUserAddress , String body , String subject){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("iamakki85@gmail.com");
        mailMessage.setTo(toUserAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);

    }


    private String generateOTP(){
        return String.format("%04d" ,new Random().nextInt(10000));
    }

}
