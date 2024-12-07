package io.security.springsecuritymaster.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

//    @Value("${jwt.key}")
    private static String secret = "D19BJbXsoP4NcRLTj9anT3jnHCSu4w83";

//    @Value("${jwt.expiration_time}")
    private static long expirationTime = 1000 * 60 * 60 * 2;

    private static final Key key = Keys.hmacShaKeyFor(secret.getBytes());;

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) throws Exception {


        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token expired", e);
        } catch (JwtException e) {
            throw new Exception("Token invalid", e);
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateEmailVerificationToken(String email) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + 5 * 60 * 1000); // 5분 유효기간

        return Jwts.builder()
                .setSubject(email)
                .claim("isEmailVerification", true)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
