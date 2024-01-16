package com.example.backend.Utils.HashingOtp;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashSpliter {

    public List<String>  splithash(String hashExp){

        return List.of(hashExp.split("\\."));
    }

}
