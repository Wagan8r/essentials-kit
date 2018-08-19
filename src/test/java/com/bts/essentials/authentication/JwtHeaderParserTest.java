package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wagan8r on 8/18/18.
 */
public class JwtHeaderParserTest extends BaseIntegrationTest {
    @Autowired
    private JwtHeaderParser jwtHeaderParser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseAuthHeader() {
        String jwt = jwtHeaderParser.parseAuthHeader("Bearer imajwt");
        assertEquals("imajwt", jwt);
    }

    @Test
    public void parseAuthHeaderHttpServletResponse() {
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletResponse.getHeader("Authorization")).thenReturn("Bearer imajwt");
        String jwt = jwtHeaderParser.parseAuthHeader(httpServletResponse);
        assertEquals("imajwt", jwt);
    }

    @Test
    public void parseAuthHeaderNullHeader() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No Authorization header provided");
        jwtHeaderParser.parseAuthHeader((String) null);
    }

    @Test
    public void parseAuthHeaderEmptyHeader() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No Authorization header provided");
        jwtHeaderParser.parseAuthHeader("");
    }

    @Test
    public void parseAuthHeaderTooManyTokens() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Authorization header is invalid. Must be of the form 'Bearer <token>'");
        jwtHeaderParser.parseAuthHeader("Bearer token token");
    }

    @Test
    public void parseAuthHeaderTooFewTokens() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Authorization header is invalid. Must be of the form 'Bearer <token>'");
        jwtHeaderParser.parseAuthHeader("Bearer");
    }

    @Test
    public void parseAuthHeaderNoBearerToken() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Authorization header is missing 'Bearer' scheme");
        jwtHeaderParser.parseAuthHeader("Barer imajwt");
    }
}
