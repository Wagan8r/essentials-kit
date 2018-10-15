package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletRequest;
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
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer imajwt");
        String jwt = jwtHeaderParser.parseAuthHeader(httpServletRequest);
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

    @Test
    public void composeAuthHeader() {
        ServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(mock(HttpServletResponse.class));
        String jwt = "Not exactly a JWT";
        jwtHeaderParser.composeAuthHeader(serverHttpResponse, jwt);
        assertEquals(String.format("Bearer %s", jwt), serverHttpResponse.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
    }
}
