package com.ludo.Snake.and.Ladder.Util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.ludo.Snake.and.Ladder.Constants
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value

import java.security.SecureRandom

@Slf4j
class Utilities {

    private static ObjectMapper mapper = new ObjectMapper()
    .tap {
        registerModule(new JavaTimeModule())
    }
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    .setTimeZone(TimeZone.getTimeZone(Constants.IND_ZONE_ID))

    static ObjectMapper objectMapper() {
        return mapper
    }

    static String generateAccessToken(Map<String, Object> claims) {
        SecureRandom secureRandom = new SecureRandom()
        byte[] keyBytes = new byte[32] // 32 bytes for a 256-bit key
        secureRandom.nextBytes(keyBytes)
        String secret = Base64.getEncoder().encodeToString(keyBytes)
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }


    private Utilities() {}

}
