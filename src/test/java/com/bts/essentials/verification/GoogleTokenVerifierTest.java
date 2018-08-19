package com.bts.essentials.verification;

import com.bts.essentials.BaseIntegrationTest;
import com.bts.essentials.model.BasicUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.GeneralSecurityException;
import java.util.Optional;

import static com.bts.essentials.testutils.DataCreation.createPayload;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Created by wagan8r on 7/12/18.
 */
public class GoogleTokenVerifierTest extends BaseIntegrationTest {
    private static final String JWT = "Not really a JWT";

    private GoogleTokenVerifier googleTokenVerifierSpy;

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        googleTokenVerifierSpy = spy(googleTokenVerifier);
    }

    @Test
    public void verifyToken() throws Exception {
        Payload payload = createPayload();
        GoogleIdToken googleIdToken = mock(GoogleIdToken.class);
        when(googleIdToken.getPayload()).thenReturn(payload);
        doReturn(googleIdToken).when(googleTokenVerifierSpy).verify(anyString(), eq(JWT));
        Optional<BasicUser> userOptional = googleTokenVerifierSpy.verifyToken(JWT);
        BasicUser user = userOptional.get();
        assertEquals(payload.getEmail(), user.getEmail());
        assertEquals(payload.get("given_name"), user.getFirstName());
        assertEquals(payload.get("family_name"), user.getLastName());
    }

    @Test
    public void verifyTokenNoMatch() throws Exception {
        doReturn(null).when(googleTokenVerifierSpy).verify(anyString(), eq(JWT));
        Optional<BasicUser> userOptional = googleTokenVerifierSpy.verifyToken(JWT);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void verifyTokenTokenParseError() throws Exception {
        doThrow(GeneralSecurityException.class).when(googleTokenVerifierSpy).verify(anyString(), eq(JWT));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Error verifying ID token");
        googleTokenVerifierSpy.verifyToken(JWT);
    }
}
