package com.bts.essentials.verification;

import com.bts.essentials.model.BasicUser;

import java.util.Optional;

/**
 * Created by wagan8r on 7/11/18.
 */
public interface TokenVerifier {
    Optional<BasicUser> verifyToken(String token);
}
