package com.bts.essentials.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wagan8r on 8/16/18.
 */
@Component
public class JwtHeaderParser {
    public static final String BEARER = "Bearer";

    public String parseAuthHeader(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
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
        if (!BEARER.equals(tokens[0])) {
            throw new RuntimeException("Authorization header is missing 'Bearer' scheme");
        }
        return tokens[1];
    }

    public void composeAuthHeader(ServerHttpResponse serverHttpResponse, String jwt) {
        serverHttpResponse.getHeaders().add(HttpHeaders.AUTHORIZATION, createBearerString(jwt));
    }

    public ResponseEntity.BodyBuilder composeAuthHeader(ResponseEntity.BodyBuilder headersBuilder, String jwt) {
        return headersBuilder.header(HttpHeaders.AUTHORIZATION, createBearerString(jwt));
    }

    private String createBearerString(String jwt) {
        return String.format("%s %s", BEARER, jwt);
    }
}
