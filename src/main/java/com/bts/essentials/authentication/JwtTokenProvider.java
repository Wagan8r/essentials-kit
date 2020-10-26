package com.bts.essentials.authentication;

import com.bts.essentials.model.IdentityProvider;
import com.bts.essentials.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by wagan8r on 8/16/18.
 */
@Component
public class JwtTokenProvider {
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String IDENTITY_PROVIDER = "identity_provider";
    private static final String ROLES = "roles";

    @Value("${essentials.jwt.secret}")
    private String jwtSecret;

    @Value("${essentials.jwt.tokenExpirationMs}")
    private Integer jwtTokenExpirationMs;

    public String getToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtTokenExpirationMs);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(EMAIL, user.getEmail())
                .claim(FIRST_NAME, user.getFirstName())
                .claim(LAST_NAME, user.getLastName())
                .claim(IDENTITY_PROVIDER, user.getIdentityProvider())
                .claim(ROLES, user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
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
        List<String> authorities = new ArrayList<>();
        Object roles = claims.get(ROLES);
        if (roles instanceof List) {
            authorities = (List<String>) roles;
        }
        return new User(UUID.fromString(claims.getSubject()),
                String.valueOf(claims.get(EMAIL)),
                String.valueOf(claims.get(FIRST_NAME)),
                String.valueOf(claims.get(LAST_NAME)),
                IdentityProvider.valueOf(String.valueOf(claims.get(IDENTITY_PROVIDER))),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
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
