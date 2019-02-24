package com.bts.essentials.authentication.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
class TestAbstractSecurityConfigurerAdapter extends AbstractSecurityConfigurerAdapter {
    @Override
    protected ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry configureAuthorizeRequests(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        return expressionInterceptUrlRegistry.antMatchers("/test/path").permitAll();
    }

    @Override
    protected void ignoreRequests(WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer) {
        ignoredRequestConfigurer.antMatchers("/unsecured/resource");
    }
}
