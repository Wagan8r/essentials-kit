package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bts.essentials.testutils.DataCreation.createUserAuthentication;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by wagan8r on 8/19/18.
 */
public class JwtAuthenticationFilterTest extends BaseIntegrationTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtHeaderParser jwtHeaderParser;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SecurityContextSetter securityContextSetter;

    @Before
    public void before() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtHeaderParser, jwtTokenProvider, securityContextSetter);
    }

    @Test
    public void doFilterInternal() throws Exception {
        Authentication userAuthentication = createUserAuthentication();
        String jwt = jwtTokenProvider.getToken(userAuthentication);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletRequest.getHeader("Authorization")).thenReturn(String.format("Bearer %s", jwt));
        FilterChain filterChain = mock(FilterChain.class);
        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userAuthentication, authentication);
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
    }
}
