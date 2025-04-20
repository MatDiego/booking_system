package com.example.booking_system.security.jwt;

import com.example.booking_system.entity.User;
import com.example.booking_system.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceImplTest {
    private JwtServiceImpl jwtService;
    private UserPrincipal testUserPrincipal;
    private final String base64EncodedSecretKey = "YWFzZGZhZHNmZGFzZmRhc2ZkYXNmYXNkZmFkZmFzZmRhc2ZkYXNmYXNkZmFzZGY=";
    private final long standardExpirationMillis = 3600000;

    @BeforeEach
    void setUp() {
        Clock systemClock = Clock.systemDefaultZone();
        jwtService = new JwtServiceImpl(base64EncodedSecretKey, standardExpirationMillis, systemClock);


        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        testUserPrincipal = new UserPrincipal(user);
    }



    @Test
    void extracts_username_from_valid_token() {
        String token = jwtService.generateToken(testUserPrincipal);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(testUserPrincipal.getUsername());
    }


    @Test
    void extracts_all_claims_from_valid_token() {
        String token = jwtService.generateToken(testUserPrincipal);

        Claims claims = jwtService.extractAllClaims(token);

        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(testUserPrincipal.getUsername());
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
    }

    @Test
    void extracts_specific_claim_using_resolver() {
        String token = jwtService.generateToken(testUserPrincipal);

        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertThat(expirationDate).isNotNull().isAfter(new Date());
        assertThat(issuedAt).isNotNull().isBeforeOrEqualTo(new Date());
        assertThat(subject).isEqualTo(testUserPrincipal.getUsername());
    }

    @Test
    void generated_token_contains_username_subject_and_valid_timestamps() {
        String token = jwtService.generateToken(testUserPrincipal);

        String extractedUsername = jwtService.extractUsername(token);
        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        String subject = jwtService.extractClaim(token, Claims::getSubject);


        assertThat(token).isNotNull().isNotEmpty();
        assertThat(extractedUsername).isEqualTo(testUserPrincipal.getUsername());
        assertThat(subject).isEqualTo(testUserPrincipal.getUsername());
        assertThat(issuedAt).isNotNull().isBeforeOrEqualTo(new Date());
        assertThat(expirationDate).as("Expiration date should be in the future")
                .isNotNull().isAfter(issuedAt);


    }

    @Test
    void generates_token_including_provided_extra_claims() {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customData", "value123");
        extraClaims.put("userId", testUserPrincipal.getUserId().toString());
        String token = jwtService.generateToken(extraClaims, testUserPrincipal);

        Claims claims = jwtService.extractAllClaims(token);
        String extractedCustomData = jwtService.extractClaim(token, c ->
                c.get("customData", String.class));
        String extractedUserId = jwtService.extractClaim(token, c ->
                c.get("userId", String.class));

        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(testUserPrincipal.getUsername());
        assertThat(extractedCustomData).isEqualTo("value123");
        assertThat(extractedUserId).isEqualTo(testUserPrincipal.getUserId().toString());
    }

    @Test
    void validates_correctly_generated_token_as_valid() {
        String token = jwtService.generateToken(testUserPrincipal);

        boolean isTokenValid = jwtService.isTokenValid(token, testUserPrincipal);

        assertThat(isTokenValid).isTrue();
    }

    @Test
    void identifies_expired_token_as_invalid() {
        Instant validationInstant = Instant.now();
        Instant generationInstant = validationInstant.minusMillis(standardExpirationMillis + 1000);
        Clock fixedGenerationClock = Clock.fixed(generationInstant, ZoneId.systemDefault());
        JwtServiceImpl serviceWithFixedClock = new JwtServiceImpl(
                base64EncodedSecretKey, standardExpirationMillis, fixedGenerationClock);
        String expiredToken = serviceWithFixedClock.generateToken(testUserPrincipal);
        Clock fixedValidationClock = Clock.fixed(validationInstant, ZoneId.systemDefault());
        JwtServiceImpl validationService = new JwtServiceImpl(
                base64EncodedSecretKey, standardExpirationMillis, fixedValidationClock
        );

        boolean isTokenValid = validationService.isTokenValid(expiredToken, testUserPrincipal);

        assertThat(isTokenValid).isFalse();
    }

    @Test
    void identifies_token_for_different_user_as_invalid() {
        String token = jwtService.generateToken(testUserPrincipal);
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        otherUser.setUsername("otheruser");
        UserPrincipal otherUserPrincipal = new UserPrincipal(otherUser);


        boolean isTokenValid = jwtService.isTokenValid(token, otherUserPrincipal);

        assertThat(isTokenValid).isFalse();
    }

}