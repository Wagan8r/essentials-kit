package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * Created by wagan8r on 8/19/18.
 */
public class JwtAuthenticationEntryPointTest extends BaseIntegrationTest {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    public void commence() throws Exception {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        AuthenticationException authenticationException = mock(AuthenticationException.class);
        jwtAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, authenticationException);

        verify(httpServletResponse, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are unauthorized to view this resource");
    }
}
