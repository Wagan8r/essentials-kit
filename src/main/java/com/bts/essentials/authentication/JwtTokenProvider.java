package com.bts.essentials.authentication;

import com.bts.essentials.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wagan8r on 8/16/18.
 */
@Component
public class JwtTokenProvider {
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    @Value("${essentials.jwt.secret}")
    private String jwtSecret;

    @Value("${essentials.jwt.tokenExpirationMs}")
    private Integer jwtTokenExpirationMs;

    public String getToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtTokenExpirationMs);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(EMAIL, user.getEmail())
                .claim(FIRST_NAME, user.getFirstName())
                .claim(LAST_NAME, user.getLastName())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public User getUser(String jwt) {
        validateJwt(jwt);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();
        return new User(UUID.fromString(claims.getSubject()), String.valueOf(claims.get(EMAIL)), String.valueOf(claims.get(FIRST_NAME)), String.valueOf(claims.get(LAST_NAME)));
    }

    protected void validateJwt(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT claims string is empty", e);
        }
    }
}
