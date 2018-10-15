package com.bts.essentials.authentication;

import com.bts.essentials.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * A utility for setting the {@link org.springframework.security.core.Authentication} on the {@link org.springframework.security.core.context.SecurityContextHolder}
 * in a consistent manner
 * <p>
 * Created by wagan8r on 10/14/18.
 */
@Component
public class SecurityContextSetter {

    /**
     * Sets the supplied {@link User} as the current {@link UserAuthentication}.
     *
     * @param user - The user
     */
    public void setAuthentication(User user) {
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUserPrincipal(user);
        userAuthentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
