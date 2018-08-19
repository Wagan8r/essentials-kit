package com.bts.essentials.authentication;

import com.bts.essentials.model.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by wagan8r on 8/18/18.
 */
@Data
public class UserAuthentication implements Authentication {
    private Collection<? extends GrantedAuthority> authorities;
    private Object credentials;
    private Object details;
    private Object principal;
    private boolean authenticated;
    private String name;

    public void setUserPrincipal(User user) {
        setPrincipal(user);
    }

    public User getUserPrincipal() {
        return (User) getPrincipal();
    }
}