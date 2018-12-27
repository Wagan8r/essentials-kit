package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import com.bts.essentials.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.bts.essentials.testutils.DataCreation.createUser;
import static com.bts.essentials.testutils.DataCreation.createUserAuthentication;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by wagan8r on 10/14/18.
 */
public class SecurityContextMutatorTest extends BaseIntegrationTest {

    @Autowired
    private SecurityContextMutator securityContextMutator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void setAuthentication() {
        User user = createUser();
        securityContextMutator.setAuthentication(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        assertEquals(user.getFirstName(), principal.getFirstName());
        assertEquals(true, authentication.isAuthenticated());
    }

    @Test
    public void getAuthenticationUser() {
        UserAuthentication userAuthentication = createUserAuthentication();
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        User authenticationUser = securityContextMutator.getAuthenticationUser();
        assertEquals(userAuthentication.getUserPrincipal(), authenticationUser);
    }

    @Test
    public void getAuthenticationUserNoAuthentication() {
        User authenticationUser = securityContextMutator.getAuthenticationUser();
        assertEquals(null, authenticationUser);
    }

    @Test
    public void getAuthenticationUserInvalidAuthentication() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Invalid Authentication has been set in the security context");
        securityContextMutator.getAuthenticationUser();
    }
}
