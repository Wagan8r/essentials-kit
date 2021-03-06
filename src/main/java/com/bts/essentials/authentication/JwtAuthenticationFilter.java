package com.bts.essentials.authentication;

import com.bts.essentials.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wagan8r on 7/21/18.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtHeaderParser jwtHeaderParser;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityContextMutator securityContextMutator;

    @Autowired
    public JwtAuthenticationFilter(JwtHeaderParser jwtHeaderParser, JwtTokenProvider jwtTokenProvider, SecurityContextMutator securityContextMutator) {
        this.jwtHeaderParser = jwtHeaderParser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityContextMutator = securityContextMutator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtHeaderParser.parseAuthHeader(request);
        User user = jwtTokenProvider.getUser(jwt);
        securityContextMutator.setAuthentication(user);
        filterChain.doFilter(request, response);
    }
}
