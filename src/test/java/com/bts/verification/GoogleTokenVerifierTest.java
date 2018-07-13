package com.bts.verification;

import com.bts.BaseUnitTest;
import com.bts.model.BasicUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Created by wagan8r on 7/12/18.
 */
public class GoogleTokenVerifierTest extends BaseUnitTest {
    private static final String JWT = "Not really a JWT";

    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleTokenVerifier googleTokenVerifier;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        httpTransport = mock(HttpTransport.class);
        jsonFactory = mock(JsonFactory.class);
        googleTokenVerifier = spy(new GoogleTokenVerifier(httpTransport, jsonFactory));
    }

    @Test
    public void verifyToken() throws Exception {
        Payload payload = createPayload();
        GoogleIdToken googleIdToken = mock(GoogleIdToken.class);
        when(googleIdToken.getPayload()).thenReturn(payload);
        doReturn(googleIdToken).when(googleTokenVerifier).verify(anyString(), eq(JWT));
        Optional<BasicUser> userOptional = googleTokenVerifier.verifyToken(JWT);
        BasicUser user = userOptional.get();
        assertEquals(payload.getEmail(), user.getEmail());
        assertEquals(payload.get("given_name"), user.getFirstName());
        assertEquals(payload.get("family_name"), user.getLastName());
    }

    @Test
    public void verifyTokenNoMatch() throws Exception {
        doReturn(null).when(googleTokenVerifier).verify(anyString(), eq(JWT));
        Optional<BasicUser> userOptional = googleTokenVerifier.verifyToken(JWT);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void verifyTokenTokenParseError() throws Exception {
        doThrow(GeneralSecurityException.class).when(googleTokenVerifier).verify(anyString(), eq(JWT));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Error verifying ID token");
        googleTokenVerifier.verifyToken(JWT);
    }

    protected Payload createPayload() {
        Payload payload = new Payload();
        payload.setEmail("nerfherder@bts.com");
        payload.set("given_name", "Han");
        payload.set("family_name", "Solo");
        return payload;
    }
}
