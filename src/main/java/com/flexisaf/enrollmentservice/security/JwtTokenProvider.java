package com.flexisaf.enrollmentservice.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.flexisaf.enrollmentservice.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);



    public String generateToken(Authentication authentication) throws UnsupportedEncodingException {

        User userPrincipal = (User)authentication.getPrincipal();

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        return JWT.create().withSubject(String.valueOf(userPrincipal.getId()))
                .withIssuedAt(new Date())
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(SecurityConstants.JWT_SECRET));



    }

}
