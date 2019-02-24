package com.bts.essentials.service;

import com.bts.essentials.BaseIntegrationTest;
import com.bts.essentials.authentication.SecurityContextMutator;
import com.bts.essentials.authentication.UserAuthentication;
import com.bts.essentials.model.BasicUser;
import com.bts.essentials.model.User;
import com.bts.essentials.verification.TokenVerifier;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wagan8r on 10/23/18.
 */
public class LoginServiceTest extends BaseIntegrationTest {
    private static final String JWT = "NOT.A.JWT";

    private LoginService loginService;

    private TokenVerifier tokenVerifier;

    @Autowired
    private SecurityContextMutator securityContextMutator;

    @Autowired
    private UsersService usersService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        tokenVerifier = mock(TokenVerifier.class);
        loginService = createLoginService(usersService);
    }

    @Test
    public void login() {
        mockSecurity();
        User user = loginService.login(JWT);
        UserAuthentication userAuthentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userAuthentication.getPrincipal(), user);
    }

    @Test
    public void getUser() {
        BasicUser basicUser = mockSecurity();
        User user = loginService.getUser(JWT);
        assertEquals(basicUser.getEmail(), user.getEmail());
        assertEquals(basicUser.getFirstName(), user.getFirstName());
        assertEquals(basicUser.getLastName(), user.getLastName());
    }

    @Test
    public void getUserNoUsersService() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No UsersService bean was supplied. Create an @Autowire-able implementation");
        mockSecurity();
        loginService = createLoginService(null);
        loginService.getUser(JWT);
    }

    @Test
    public void getUserInvalidToken() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Invalid user token");
        loginService.getUser(JWT);
    }

    protected BasicUser mockSecurity() {
        BasicUser basicUser = createBasicUserObject();
        when(tokenVerifier.verifyToken(JWT)).thenReturn(Optional.of(basicUser));
        return basicUser;
    }

    protected LoginService createLoginService(UsersService usersService) {
        List<TokenVerifier> tokenVerifiers = ImmutableList.of(tokenVerifier);
        return new LoginService(tokenVerifiers, securityContextMutator, Optional.ofNullable(usersService));
    }
}
