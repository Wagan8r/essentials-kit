package com.bts.essentials.authentication;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wagan8r on 8/16/18.
 */
@Component
public class JwtHeaderParser {

    public String parseAuthHeader(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        return parseAuthHeader(authHeader);
    }

    public String parseAuthHeader(String authHeader) {
        if (StringUtils.isEmpty(authHeader)) {
            throw new RuntimeException("No Authorization header provided");
        }
        String[] tokens = authHeader.split(" ");
        if (tokens.length != 2) {
            throw new RuntimeException("Authorization header is invalid. Must be of the form 'Bearer <token>'");
        }
        if (!"Bearer".equals(tokens[0])) {
            throw new RuntimeException("Authorization header is missing 'Bearer' scheme");
        }
        return tokens[1];
    }
}
