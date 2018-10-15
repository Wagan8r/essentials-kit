package com.bts.essentials.authentication;

import com.bts.essentials.BaseIntegrationTest;
import com.bts.essentials.model.User;
import com.bts.essentials.testutils.DataCreation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertEquals;

/**
 * Created by wagan8r on 10/14/18.
 */
public class SecurityContextSetterTest extends BaseIntegrationTest {

    @Autowired
    private SecurityContextSetter securityContextSetter;

    @Test
    public void setAuthentication() {
        User user = DataCreation.createUser();
        securityContextSetter.setAuthentication(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        assertEquals(user.getFirstName(), principal.getFirstName());
        assertEquals(true, authentication.isAuthenticated());
    }
}
