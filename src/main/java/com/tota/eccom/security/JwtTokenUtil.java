package com.tota.eccom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60; // 2 horas
    private static final long REFRESH_TOKEN_VALIDITY = 6 * 60 * 60; // 6 horas

    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), JWT_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(removeBearer(token), Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(removeBearer(token));
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(removeBearer(token)).getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(removeBearer(token), Claims::getExpiration);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(removeBearer(token));
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(removeBearer(token)));
    }

    public boolean isRefreshTokenValid(String token) {
        return !isTokenExpired(removeBearer(token));
    }

    private String removeBearer(String token) {
        return token.replace("Bearer ", "");
    }
}
