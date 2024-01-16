package com.example.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT_KEY}")
    String jwtKey;

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
