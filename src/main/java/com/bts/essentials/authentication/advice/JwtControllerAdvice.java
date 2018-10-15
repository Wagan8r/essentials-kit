package com.bts.essentials.authentication.advice;

import com.bts.essentials.authentication.JwtHeaderParser;
import com.bts.essentials.authentication.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;

/**
 * A {@link ControllerAdvice} class that takes the current {@link Authentication} from the {@link SecurityContextHolder} and generates a new JWT
 * and places it in the Authorization header of the response. {@link org.springframework.stereotype.Controller} methods may opt-out of the
 * header processing by placing the {@link ExcludeJwtControllerAdvice} annotation on the desired methods.
 *
 * Created by wagan8r on 9/2/18.
 */
@ControllerAdvice
public class JwtControllerAdvice implements ResponseBodyAdvice {

    private final JwtHeaderParser jwtHeaderParser;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtControllerAdvice(JwtHeaderParser jwtHeaderParser, JwtTokenProvider jwtTokenProvider) {
        this.jwtHeaderParser = jwtHeaderParser;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return Arrays.stream(returnType.getMethodAnnotations()).noneMatch(annotation -> annotation instanceof ExcludeJwtControllerAdvice);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String jwt = jwtTokenProvider.getToken(authentication);
            jwtHeaderParser.composeAuthHeader(response, jwt);
        }
        return body;
    }
}
