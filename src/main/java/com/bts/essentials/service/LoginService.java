package com.bts.essentials.service;

import com.bts.essentials.authentication.SecurityContextMutator;
import com.bts.essentials.model.BasicUser;
import com.bts.essentials.model.User;
import com.bts.essentials.verification.TokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by wagan8r on 10/23/18.
 */
@Component
public class LoginService {
    private final List<TokenVerifier> tokenVerifiers;
    private final SecurityContextMutator securityContextMutator;
    private final UsersService usersService;

    @Autowired()
    public LoginService(List<TokenVerifier> tokenVerifiers, SecurityContextMutator securityContextMutator, Optional<UsersService> usersService) {
        this.tokenVerifiers = tokenVerifiers;
        this.securityContextMutator = securityContextMutator;
        this.usersService = usersService.orElse(null);
    }

    public User login(String jwt) {
        User user = getUser(jwt);
        securityContextMutator.setAuthentication(user);
        return user;
    }

    public User getUser(String jwt) {
        Optional<BasicUser> basicUserOptional;
        for (TokenVerifier tokenVerifier : tokenVerifiers) {
            basicUserOptional = tokenVerifier.verifyToken(jwt);
            if (basicUserOptional.isPresent()) {
                if (usersService == null) {
                    throw new RuntimeException("No UsersService bean was supplied. Create an @Autowire-able implementation");
                }
                return usersService.getOrCreateUser(basicUserOptional.get());
            }
        }
        throw new RuntimeException("Invalid user token");
    }
}
