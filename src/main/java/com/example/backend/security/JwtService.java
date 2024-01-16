package com.example.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class JwtService {



    String jwtKey="aotghldfvasfblkbjdxmcjhbdvnljhkvmdvbnvJvzvbxvjkdlmvzvbndlxz";

    private final Algorithm algorithm = Algorithm.HMAC256(jwtKey);

    public String createJwt(String userId){
        return JWT.create()
                .withSubject(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(Instant.ofEpochSecond(System.currentTimeMillis()+600000))
                .sign(algorithm);
    }


    public String retrieveUserId(String jwtKey){
        var decodedJwt = JWT.decode(jwtKey);
        var userId = decodedJwt.getSubject();

        return userId;
    }



}
