package com.bts.essentials.authentication.config;

import com.bts.essentials.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by wagan8r on 7/29/18.
 */
@Configuration
@EnableWebSecurity
@EnableWebMvc
public abstract class AbstractSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtHeaderParser jwtHeaderParser;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SecurityContextSetter securityContextSetter;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtHeaderParser, jwtTokenProvider, securityContextSetter);
    }

    /**
     * This prevents Spring Boot from automatically adding the {@link JwtAuthenticationFilter} to the filter chain
     */
    @Bean
    public RegistrationBean jwtAuthenticationFilterRegistrationBlocker(JwtAuthenticationFilter jwtAuthenticationFilter) {
        return blockBeanRegistration(jwtAuthenticationFilter);
    }

    /**
     * This prevents Spring Boot from automatically adding a {@link GenericFilterBean} to the filter chain
     */
    protected RegistrationBean blockBeanRegistration(GenericFilterBean genericFilterBean) {
        RegistrationBean registrationBean = new FilterRegistrationBean(genericFilterBean);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        configureAuthorizeRequests(httpSecurity
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()).anyRequest().authenticated();

        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        ignoreRequests(webSecurity.ignoring());
    }

    protected abstract ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry configureAuthorizeRequests(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry);

    protected abstract void ignoreRequests(WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer);
}
