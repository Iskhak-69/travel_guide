package alatoo.travel_guide.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

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

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private SecretKey getSecretKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}