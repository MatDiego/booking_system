package com.example.booking_system.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private final SecretKey signingKey;
    private final long jwtExpirationMillis;
    private final Clock clock;

    public JwtServiceImpl(String base64SecretKey, long jwtExpirationMillis, Clock clock) {
        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpirationMillis = jwtExpirationMillis;
        this.clock = clock;
    }

    @Autowired
    public JwtServiceImpl(
            @Value("${secret-key}") String base64SecretKey,
            @Value("${jwt.expiration.ms:86400000}") long expirationMillis
    ) {
        this(base64SecretKey, expirationMillis, Clock.systemDefaultZone());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        Instant now = clock.instant();
        Date issuedAtDate = Date.from(now);
        Date expirationDate = Date.from(now.plusMillis(this.jwtExpirationMillis));

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedAtDate)
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (JwtException e)  {
            return false;
        }

        }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
