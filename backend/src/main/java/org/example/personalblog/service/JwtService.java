package org.example.personalblog.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${ACCESS_EXPIRATION}")
    private int accessTokenExpiration;
    private final KeyPair keyPair;

    public String getAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
        String roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(accessTokenExpiration)))
                .claim("roles", roles)
                .signWith(keyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getTokenFromHeader(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
