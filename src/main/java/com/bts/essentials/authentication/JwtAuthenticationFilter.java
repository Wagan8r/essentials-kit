package com.bts.essentials.authentication;

import com.bts.essentials.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wagan8r on 7/21/18.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtHeaderParser jwtHeaderParser;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtHeaderParser jwtHeaderParser, JwtTokenProvider jwtTokenProvider) {
        this.jwtHeaderParser = jwtHeaderParser;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtHeaderParser.parseAuthHeader(request);
        User user = jwtTokenProvider.getUser(jwt);
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUserPrincipal(user);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        filterChain.doFilter(request, response);
    }
}
