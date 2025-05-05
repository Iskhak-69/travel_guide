package alatoo.travel_guide.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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
                .signWith(getSecretKey(secretKey), SignatureAlgorithm.HS256)
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
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(getSecretKey(verificationKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSecretKey(verificationKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid verification token", e);
        }
    }

    public String getEmailFromExpiredToken(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSecretKey(verificationKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse token", e);
        }
    }

    private SecretKey getSecretKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
