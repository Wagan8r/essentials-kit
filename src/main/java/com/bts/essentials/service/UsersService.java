package com.bts.essentials.service;

import com.bts.essentials.model.BasicUser;
import com.bts.essentials.model.User;

/**
 * Created by wagan8r on 10/23/18.
 */
public interface UsersService {
    User getOrCreateUser(BasicUser basicUser);
}
