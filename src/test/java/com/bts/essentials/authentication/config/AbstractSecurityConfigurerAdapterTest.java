package com.bts.essentials.authentication.config;

import com.bts.essentials.BaseMvcTest;
import com.bts.essentials.authentication.JwtHeaderParser;
import com.bts.essentials.authentication.JwtTokenProvider;
import com.bts.essentials.authentication.advice.ExcludeJwtControllerAdvice;
import org.apache.http.HttpHeaders;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.bts.essentials.testutils.DataCreation.createUserAuthentication;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

@Controller
class TestController {
    @RequestMapping(value = "/unsecured/resource", method = RequestMethod.GET)
    public ResponseEntity<String> unsecuredEndpoint() {
        return ResponseEntity.ok("You got in!");
    }

    @RequestMapping(value = "/secured/resource", method = RequestMethod.GET)
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("You had a jwt");
    }

    @RequestMapping(value = "/excluded/resource", method = RequestMethod.GET)
    @ExcludeJwtControllerAdvice
    public ResponseEntity<String> excludedEndpoint() {
        return ResponseEntity.ok("Didn't need a jwt");
    }
}

/**
 * Created by wagan8r on 8/19/18.
 */
public class AbstractSecurityConfigurerAdapterTest extends BaseMvcTest {

    @Autowired
    private TestAbstractSecurityConfigurerAdapter testAbstractSecurityConfigurerAdapter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        initializeMockMvc();
    }

    @Test
    public void configure() throws Exception {
        assertNotNull(testAbstractSecurityConfigurerAdapter);
    }

    @Test
    public void permitTestPath() throws Exception {
        mockMvc.perform(get("/unsecured/resource"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You got in!")))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, (String) null));
    }

    @Test
    public void filterTestPath() throws Exception {
        Authentication userAuthentication = createUserAuthentication();
        String jwt = jwtTokenProvider.getToken(userAuthentication);
        mockMvc.perform(get("/secured/resource").header(HttpHeaders.AUTHORIZATION, String.format("%s %s", JwtHeaderParser.BEARER, jwt)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You had a jwt")))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, containsString("Bearer")));
    }

    @Test
    public void filterTestPathNoJwt() throws Exception {
        //TODO: make this expect a 400 level error or something
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No Authorization header provided");
        mockMvc.perform(get("/secured/resource"));
    }

    @Test
    public void jwtAdviceSetsAuthorizationHeader() throws Exception {
        Authentication userAuthentication = createUserAuthentication();
        String jwt = jwtTokenProvider.getToken(userAuthentication);
        mockMvc.perform(get("/excluded/resource").header(HttpHeaders.AUTHORIZATION, String.format("%s %s", JwtHeaderParser.BEARER, jwt)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Didn't need a jwt")))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, (String) null));
    }
}
