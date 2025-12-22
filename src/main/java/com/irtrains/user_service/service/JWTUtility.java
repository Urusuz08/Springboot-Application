package com.irtrains.user_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.irtrains.user_service.DTO.*;

import java.security.Key;
import java.util.Date;

@Service
public class JWTUtility {

    @Value("${jwt.secret.key}") private String SECRET;
    @Value("${jwt.expiration.ms}") private long EXPIRATION_TIME;

    // --- Token Generation ---
    public String generateToken(LoginDTO authentication) {
        // Simplified payload (you'd add roles/authorities here)
        return Jwts.builder()
                .setSubject(authentication.getUsername())
                .claim("role", authentication.getType())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- Key and Claim Extraction ---
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        // Simplified extraction
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        // Check signature and expiration
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Handle ExpiredJwtException, SignatureException, etc.
            return false;
        }
    }
}
