package com.example.backend.Utils.HashingOtp;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;

@Service
public class HashingOtp {

    public String generateHmacSHA256(  String data , String secretKey ) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(),"HmacSHA256");
        sha256.init(secretKeySpec);

        byte [] digest = sha256.doFinal(data.getBytes());

        Long expiration = System.currentTimeMillis()+2*60*1000;

        StringBuilder result = new StringBuilder();
        for (byte b : digest){
            result.append(String.format("%02x",b));
        }

        result.append(".").append(expiration);


         return  result.toString();
    }
}
