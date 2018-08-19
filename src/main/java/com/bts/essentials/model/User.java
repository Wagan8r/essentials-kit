package com.bts.essentials.model;

import lombok.Data;

import java.util.UUID;

/**
 * Created by wagan8r on 8/18/18.
 */
@Data
public class User extends BasicUser {
    private UUID id;

    public User(UUID id, String email, String firstName, String lastName) {
        super(email, firstName, lastName);
        this.id = id;
    }
}
