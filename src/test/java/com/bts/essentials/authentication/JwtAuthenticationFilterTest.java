package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import com.bts.essentials.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bts.essentials.testutils.DataCreation.createUser;
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
    private SecurityContextMutator securityContextMutator;

    @Before
    public void before() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtHeaderParser, jwtTokenProvider, securityContextMutator);
    }

    @Test
    public void doFilterInternal() throws Exception {
        User user = createUser();
        String jwt = jwtTokenProvider.getToken(user);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletRequest.getHeader("Authorization")).thenReturn(String.format("Bearer %s", jwt));
        FilterChain filterChain = mock(FilterChain.class);
        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        User authenticationUser = securityContextMutator.getAuthenticationUser();
        assertEquals(user, authenticationUser);
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
    }
}
