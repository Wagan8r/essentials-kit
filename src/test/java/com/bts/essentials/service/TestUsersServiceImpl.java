package com.bts.essentials.service;

import com.bts.essentials.model.BasicUser;
import com.bts.essentials.model.User;
import com.google.common.collect.ImmutableList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class TestUsersServiceImpl implements UsersService {

    @Override
    public User getOrCreateUser(BasicUser basicUser) {
        return new User(UUID.randomUUID(), basicUser.getEmail(), basicUser.getFirstName(), basicUser.getLastName(),
                basicUser.getIdentityProvider(), ImmutableList.of(new SimpleGrantedAuthority("TEST_ROLE")));
    }
}
