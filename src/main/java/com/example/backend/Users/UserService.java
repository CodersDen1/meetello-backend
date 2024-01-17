package com.example.backend.Users;

import com.example.backend.Users.dtos.GenerateOTPRequest;
import com.example.backend.Users.dtos.VerifyOtpRequest;
import com.example.backend.Utils.HashingOtp.HashSpliter;
import com.example.backend.Utils.HashingOtp.HashingOtp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ClientInfoStatus;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final  SmsService smsService;
    private final HashingOtp hashingOtp;


    @Value("${hash.key}")
    String HASH_KEY;
    public UserService(UserRepository userRepository , ModelMapper modelMapper, JavaMailSender javaMailSender, SmsService smsService, HashingOtp hashingOtp, HashSpliter hashSpliter) {
        this.modelMapper= modelMapper;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.smsService = smsService;
        this.hashingOtp = hashingOtp;
    }

    public void generateAndSendOTP(GenerateOTPRequest req) throws NoSuchAlgorithmException, InvalidKeyException {
        String otp= generateOTP();
        getUserByEmailOrPhoneNumber(req,otp);
    }

    public boolean verifyOtp(VerifyOtpRequest req) throws NoSuchAlgorithmException, InvalidKeyException {
       if(req.getEmail()!=null){
           UserEntity user = userRepository.findByEmail(req.getEmail()).orElseThrow(()-> new RuntimeException("User not found"));

           //from database
           List<String> storedDatabaseHashes = List.of(user.getOtp().split("\\."));
//           for(String h: storedDatabaseHashes){
//               System.out.println(h);
//           }
           String storedHash = storedDatabaseHashes.get(0);
           long storedExpiry = Long.parseLong(storedDatabaseHashes.get(1));

           //requested body
           List<String> requestBodyHash = List.of(hashingOtp.generateHmacSHA256(req.getOtp(), HASH_KEY).split("\\."));
//           for (String h:
//                requestBodyHash) {
//               System.out.println(h);
//           }
            String requestHash = requestBodyHash.get(0);
            long currentTime = System.currentTimeMillis();


            //logic to check if they are equal
           return req.getOtp() != null && storedHash.equals(requestHash);


       } else if (req.getPhoneNumber()!=null) {
           UserEntity user = userRepository.findByPhoneNumber(req.getPhoneNumber()).orElseThrow(()-> new RuntimeException("User not found"));

           //from database
           List<String> storedDatabaseHashes = List.of(user.getOtp().split("\\."));
//           for(String h: storedDatabaseHashes){
//               System.out.println(h);
//           }
           String storedHash = storedDatabaseHashes.get(0);
           long storedExpiry = Long.parseLong(storedDatabaseHashes.get(1));

           //requested body
           List<String> requestBodyHash = List.of(hashingOtp.generateHmacSHA256(req.getOtp(), HASH_KEY).split("\\."));
//           for (String h:
//                requestBodyHash) {
//               System.out.println(h);
//           }
           String requestHash = requestBodyHash.get(0);
           long currentTime = System.currentTimeMillis();

           //logic to check if they are equal
           return req.getOtp() != null && storedHash.equals(requestHash);

       }
        return false;
    }





    private void getUserByEmailOrPhoneNumber(GenerateOTPRequest request, String otp) throws NoSuchAlgorithmException, InvalidKeyException {
        if (request.getEmail() != null) {
            UserEntity existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (existingUser == null) {
                // Create a new user if not exists
                UserEntity newUser = modelMapper.map(request, UserEntity.class);
                sendEmailOTP(request.getEmail(), otp);
                newUser.setEmail(request.getEmail());

                newUser.setOtp(hashingOtp.generateHmacSHA256(otp ,HASH_KEY));

                userRepository.save(newUser);
            } else {
                // Update the existing user

                sendEmailOTP(request.getEmail(), otp);

                existingUser.setOtp(hashingOtp.generateHmacSHA256(otp ,HASH_KEY));
                userRepository.save(existingUser);
            }
        } else if (request.getPhoneNumber() != null) {
            UserEntity existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
            if (existingUser == null) {
                // Create a new user if not exists
                UserEntity newUser = modelMapper.map(request, UserEntity.class);
                newUser.setPhoneNumber(request.getPhoneNumber());
                sendMobileOTP(request.getPhoneNumber(), otp);
                newUser.setOtp(hashingOtp.generateHmacSHA256(otp ,HASH_KEY));
                userRepository.save(newUser);
            } else {
                // Update the existing user
                sendMobileOTP(request.getPhoneNumber(), otp);
                existingUser.setOtp(hashingOtp.generateHmacSHA256(otp ,HASH_KEY));
                userRepository.save(existingUser);
            }
        } else {
            throw new IllegalArgumentException("Email or phone number must be provided");
        }
    }



    public UserEntity getUser(String userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        return  user;
    }

    public  UserEntity getUserByEmailOrPhone(String email , String phone){
        if(email!=null){
            return userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User email not found"));
        }
        else if(phone!=null){
            return userRepository.findByPhoneNumber(phone).orElseThrow(()-> new RuntimeException("Phone Number not found"));
        }
        else {
         throw new RuntimeException("kindly prpvide sufficient args");
        }
    }






    public void sendMobileOTP(String userAddress ,String otp){

            smsService.sendOtpSms(userAddress,otp);

    }

    public void sendEmailOTP(String toUserAddress , String otp ){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("iamakki85@gmail.com");
        mailMessage.setTo(toUserAddress);
        mailMessage.setSubject("your OTP for login to meetello!!");
        mailMessage.setText("thank you for choosing us here is your otp for continuing : \t"+otp );
        javaMailSender.send(mailMessage);

    }








    private String generateOTP(){
        return String.format("%04d" ,new Random().nextInt(10000));
    }

}
