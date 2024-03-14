package org.nadarkanloev.vktest.Controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nadarkanloev.vktest.DTO.JwtAuthenticationResponse;
import org.nadarkanloev.vktest.DTO.SignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerSignInTest {

    @Autowired
    private AuthController authController;

    @Disabled //не работают, когда applicatiom.yaml настроен под докер
    @Test
    void signedInAndShouldReturnJWTToken() throws Exception{

        SignInRequest signInRequest = new SignInRequest("Nadar", "Nadar");

        ResponseEntity<JwtAuthenticationResponse> response = authController.signIn(signInRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(JwtAuthenticationResponse.class, response.getBody().getClass());
    }
}
