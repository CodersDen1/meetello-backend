package com.example.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class JwtService {


    @Value("${jwt.key}")
    String jwtKey="bkjasdbiqbesjkdbaksdhaLSJFASBLAKJSDAMSHBDASDAHSDNASMDBASMDBASMBDVASFLGASLDCASKFHVBASFNASDADA";
    String refreshJwtJKey = "refreshsecretkeyaoshefgaskfjnaljgsdjalgbaskandaggnhasfsdagbasjdagasdcasd";

    private final Algorithm algorithm = Algorithm.HMAC256(jwtKey);
    private final Algorithm refreshAlgorithms = Algorithm.HMAC256(refreshJwtJKey);


    public Algorithm refreshAlgorithm() {
        return Algorithm.HMAC384(jwtKey.getBytes());  // Use HMAC384 algorithm for refresh tokens
    }

    public Algorithm retrieveAlgorithm(String jwtKey) {
        DecodedJWT decodedJWT = JWT.decode(jwtKey);

        // Check if the token has the "tokenType" claim indicating it's a refresh token
        if (decodedJWT.getClaim("tokenType").asString().equals("refresh")) {
            return refreshAlgorithm();
        }

        // Assume it's an access token if the "tokenType" claim is not present
        return algorithm;
    }


    public String createJwt(String userId){
        return JWT.create()
                .withSubject(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(Instant.ofEpochSecond(System.currentTimeMillis()+600000))
                .sign(algorithm);
    }

    public String createRefreshJWT(String userId){
        return JWT.create()
                .withSubject(userId)
                .withClaim("tokenType","refresh")
                .withIssuedAt(new Date())
                .withExpiresAt(Instant.ofEpochSecond(System.currentTimeMillis()+7*24*60*60*1000))
                .sign(refreshAlgorithms);
    }



    public String retrieveUserId(String jwtKey){
        var decodedJwt = JWT.decode(jwtKey);
        var userId = decodedJwt.getSubject();

        return userId;
    }



}
