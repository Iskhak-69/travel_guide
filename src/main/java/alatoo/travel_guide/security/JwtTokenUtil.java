package alatoo.travel_guide.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private final String verificationKey = generateVerificationKey(64);

    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateToken(String username) {
        return createToken(Map.of(), username);
    }

    public static String generateVerificationKey(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(byteLength * 8, secureRandom).toString(16);
    }

    public String generateVerificationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, verificationKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(verificationKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid verification token", e);
        }
    }

    public String getEmailFromExpiredToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(verificationKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse token", e);
        }
    }
}
