package com.cumulocity.model.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CumulocityOAuthCredentialsTest {

    private static final String SAMPLE_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOm51bGwsImlzcyI6Im9hdXRoLn" +
            "N0YWdpbmctbGF0ZXN0LmM4eS5pbyIsImF1ZCI6Im9hdXRoLnN0YWdpbmctbGF0ZXN0LmM4eS5pbyIsInN1YiI6ImxhcmEiLCJpYXQiOjE1" +
            "NDU5OTA5ODcsIm5iZiI6bnVsbCwiZXhwIjoxNTQ1OTk0NTg3LCJ0ZW4iOiJvYXV0aCIsInhzcmZUb2tlbiI6IlNKWlBJUlBORGJ3T1Rmen" +
            "dqZFJkIn0.ZUkBibmqJftF1lSMtSLKAs_KQYJw3QbiplNdnyTrwyoASsFfKHja_ywHQnypWYDKGx062Uc8x6OkcoGefSgsZQ";

    @Test
    public void shouldReadTenantIdCorrectly() {
        CumulocityOAuthCredentials credentials = CumulocityOAuthCredentials.builder()
                .oAuthAccessToken(SAMPLE_TOKEN)
                .build();

        assertEquals("oauth", credentials.getTenantId());
    }

    @Test
    public void shouldReadUsernameCorrectly() {
        CumulocityOAuthCredentials credentials = CumulocityOAuthCredentials.builder()
                .oAuthAccessToken(SAMPLE_TOKEN)
                .build();

        assertEquals("lara", credentials.getUsername());
    }

    @Test
    public void shouldReturnAccessTokenAsAuthenticationString() {
        CumulocityOAuthCredentials credentials = CumulocityOAuthCredentials.builder()
                .oAuthAccessToken(SAMPLE_TOKEN)
                .build();

        assertEquals(SAMPLE_TOKEN, credentials.getAuthenticationString());
    }
}
