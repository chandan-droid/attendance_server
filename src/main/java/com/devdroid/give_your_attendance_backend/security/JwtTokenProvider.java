package com.devdroid.give_your_attendance_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        //Converts your secret string into a HMAC-SHA key suitable for signing JWTs.
        //Uses io.jsonwebtoken.security.Keys (from the jjwt library).
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        //this token is usually returned to the client after login.
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        //Parses the token to verify its signature and extract payload (Claims).
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        //Returns the user ID that was set as the subject when generating the token.
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}


//JWT (JSON Web Token) has three parts:
//Header.Payload.Signature
//For example:
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NSIsImVtYWlsIjoiam9obkBkb2UuY29tIn0.lR3WURCOTqFZ2Dfw6RWBZc2W4oUMpLaVUzZrj2wzv6E

//Let’s decode it:
//Header: tells what algorithm is used (e.g., HS256 → HMAC SHA256)
//Payload: contains your data (user id, email, roles, etc.)
//Signature: is a hash created using your secret key.