package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import com.bts.essentials.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static com.bts.essentials.testutils.DataCreation.createUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by wagan8r on 8/18/18.
 */
public class JwtTokenProviderTest extends BaseIntegrationTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${essentials.jwt.secret}")
    private String jwtSecret;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private UserAuthentication userAuthentication;

    @Before
    public void before() {
        user = createUser();
        userAuthentication = new UserAuthentication();
        userAuthentication.setUserPrincipal(user);
    }

    @Test
    public void getToken() {
        String jwt = jwtTokenProvider.getToken(userAuthentication);
        assertNotNull(jwt);
    }

    @Test
    public void getTokenInvalidPrincipal() {
        userAuthentication.setPrincipal("Totally a junk value");
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Invalid Authentication principal");
        jwtTokenProvider.getToken(userAuthentication);
    }

    @Test
    public void getUser() {
        String jwt = jwtTokenProvider.getToken(userAuthentication);
        User jwtUser = jwtTokenProvider.getUser(jwt);
        assertEquals(user.getId(), jwtUser.getId());
        assertEquals(user.getEmail(), jwtUser.getEmail());
        assertEquals(user.getFirstName(), jwtUser.getFirstName());
        assertEquals(user.getLastName(), jwtUser.getLastName());
    }

    @Test
    public void validateJwt() {
        String jwt = jwtTokenProvider.getToken(userAuthentication);
        jwtTokenProvider.validateJwt(jwt);
    }

    @Test
    public void validateJwtInvalidSignature() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Invalid JWT signature");
        String badJwt = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(SignatureAlgorithm.HS512, "total junk")
                .compact();
        jwtTokenProvider.validateJwt(badJwt);
    }

    @Test
    public void validateJwtInvalidToken() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Invalid JWT token");
        jwtTokenProvider.validateJwt("bogus");
    }

    @Test
    public void validateJwtExpiredToken() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Expired JWT token");
        String badJwt = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setExpiration(new Date(new Date().getTime() - 1000))
                .compact();
        jwtTokenProvider.validateJwt(badJwt);
    }

    @Test
    public void validateJwtEmptyClaimsToken() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("JWT claims string is empty");
        jwtTokenProvider.validateJwt("");
    }
}
