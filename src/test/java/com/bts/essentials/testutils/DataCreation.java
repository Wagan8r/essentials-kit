package com.bts.essentials.testutils;

import com.bts.essentials.authentication.UserAuthentication;
import com.bts.essentials.model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import java.util.UUID;

/**
 * Created by wagan8r on 8/18/18.
 */
public class DataCreation {

    public static UserAuthentication createUserAuthentication() {
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUserPrincipal(createUser());
        userAuthentication.setAuthenticated(true);
        return userAuthentication;
    }

    public static User createUser() {
        return new User(UUID.randomUUID(), "nerfherder@bts.com", "Han", "Solo");
    }

    public static Payload createPayload() {
        User user = createUser();
        Payload payload = new Payload();
        payload.setEmail(user.getEmail());
        payload.set("given_name", user.getFirstName());
        payload.set("family_name", user.getLastName());
        return payload;
    }
}
