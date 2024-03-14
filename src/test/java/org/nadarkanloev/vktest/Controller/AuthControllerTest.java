package org.nadarkanloev.vktest.Controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.nadarkanloev.vktest.DTO.JwtAuthenticationResponse;
import org.nadarkanloev.vktest.DTO.SignInRequest;
import org.nadarkanloev.vktest.DTO.SignUpRequest;
import org.nadarkanloev.vktest.Model.User;
import org.nadarkanloev.vktest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.nadarkanloev.vktest.Enum.Role.ROLE_ADMIN;
import static org.nadarkanloev.vktest.Enum.Role.ROLE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Disabled //не работают, когда applicatiom.yaml настроен под докер
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthController authController;
    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() throws Exception{
        assertThat(authController).isNotNull();
    }
    @Test
    void shouldReturnUserRegisteredSuccess() throws Exception{
        SignUpRequest signUpRequest = new SignUpRequest("user1", "user12@mail.ru", "user1");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        when(userRepository.save(any(User.class))).thenReturn(null);

        this.mockMvc.perform(post("http://localhost:8080/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
