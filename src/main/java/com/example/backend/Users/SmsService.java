package com.example.backend.Users;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;


    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    private  final UserRepository userRepository;


    private final TwilioRestClient twilioRestClient(){
        return  new TwilioRestClient.Builder(accountSid,authToken).build();
    }



    public SmsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    public void sendOtpSms(String phoneNumber , String otp){
        try{

            TwilioRestClient twilioRestClient = twilioRestClient();
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your OTP is"+otp
            ).create();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
