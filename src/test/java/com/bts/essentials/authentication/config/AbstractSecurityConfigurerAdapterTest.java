package com.bts.essentials.authentication.config;

import com.bts.essentials.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.mockito.Mockito.mock;

@Component
class TestAbstractSecurityConfigurerAdapter extends AbstractSecurityConfigurerAdapter {

    @Override
    protected ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry configureAuthorizeRequests(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        return expressionInterceptUrlRegistry.antMatchers("/test/path").permitAll();
    }
}

/**
 * Created by wagan8r on 8/19/18.
 */
public class AbstractSecurityConfigurerAdapterTest extends BaseIntegrationTest {

    @Autowired
    private TestAbstractSecurityConfigurerAdapter testAbstractSecurityConfigurerAdapter;

    @Test
    public void configure() throws Exception {
        ObjectPostProcessor objectPostProcessor = mock(ObjectPostProcessor.class);
        AuthenticationManagerBuilder authenticationManagerBuilder = mock(AuthenticationManagerBuilder.class);
        HttpSecurity httpSecurity = new HttpSecurity(objectPostProcessor, authenticationManagerBuilder, Collections.EMPTY_MAP);
        testAbstractSecurityConfigurerAdapter.configure(httpSecurity);
    }
}
