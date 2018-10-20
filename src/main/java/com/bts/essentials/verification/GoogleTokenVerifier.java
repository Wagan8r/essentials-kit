package com.bts.essentials.verification;

import com.bts.essentials.model.BasicUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${essentials.client.ids.google}")
    private List<String> clientIds;

    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    @Autowired
    public GoogleTokenVerifier(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this.httpTransport = httpTransport;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public Optional<BasicUser> verifyToken(String token) {
        return clientIds.stream().map(clientId -> verifyToken(clientId, token)).filter(Objects::nonNull).findFirst();
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

    /**
     * Moved to its own method due to the inability to either mock GoogleIdTokenVerifier or supply a valid token at test
     * time
     */
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
