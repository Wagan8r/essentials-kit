package com.bts.essentials.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wagan8r on 8/18/18.
 */
@Data
public class User extends BasicUser {
    private UUID id;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public User() {
    }

    public User(UUID id, String email, String firstName, String lastName, IdentityProvider identityProvider, List<GrantedAuthority> authorities) {
        super(email, firstName, lastName, identityProvider);
        this.id = id;
        this.authorities = authorities;
    }
}
