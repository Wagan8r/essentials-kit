package com.bts.verification;

import com.bts.model.BasicUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by wagan8r on 7/11/18.
 */
@Component
public class GoogleTokenVerifier implements TokenVerifier {
    //******************************** DO NOT KEEP IN HERE ********************************
    private static final List<String> CLIENT_IDS = ImmutableList.of("871115837782-vj0omqf5n89so84idvmsrme0k4epmidg.apps.googleusercontent.com");
    //**** SHOULD BE STORED IN A CONFIG FILE OR SOMETHING. PERHAPS A DISCOVERY SERVICE ****

    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    @Autowired
    public GoogleTokenVerifier(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this.httpTransport = httpTransport;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public Optional<BasicUser> verifyToken(String token) {
        return CLIENT_IDS.stream().map(clientId -> verifyToken(clientId, token)).filter(Objects::nonNull).findFirst();
    }

    public BasicUser verifyToken(String clientId, String token) {
        GoogleIdToken idToken = null;
        try {
            idToken = verify(clientId, token);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying ID token", e);
        }
        BasicUser basicUser = null;
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            basicUser = createUser(payload);
        }
        return basicUser;
    }

    protected GoogleIdToken verify(String clientId, String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
        return verifier.verify(token);
    }

    protected BasicUser createUser(Payload payload) {
        return new BasicUser(payload.getEmail(), (String) payload.get("given_name"), (String) payload.get("family_name"));
    }
}
