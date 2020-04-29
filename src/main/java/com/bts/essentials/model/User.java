package com.bts.essentials.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Created by wagan8r on 8/18/18.
 */
@Data
public class User extends BasicUser {
    private UUID id;
    private List<Role> roles;

    public User(UUID id, String email, String firstName, String lastName, IdentityProvider identityProvider) {
        super(email, firstName, lastName, identityProvider);
        this.id = id;
    }
}
