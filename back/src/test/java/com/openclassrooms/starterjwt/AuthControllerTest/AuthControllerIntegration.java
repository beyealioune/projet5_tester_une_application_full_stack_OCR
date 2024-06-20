package com.openclassrooms.starterjwt.AuthControllerTest;

import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegration {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * MockBean for simulating the UserRepository in the integration tests.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked password encoder for encoding and decoding passwords.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * MockBean for simulating the AuthenticationManager in the integration tests.
     */
    @MockBean
    private AuthenticationManager authenticationManager;


    @Test
    @Tag("api/auth/register api/auth/login")
    public void registerAndLoginCredentialValidTest() throws Exception {
        String email = "test7@test.com";
        String password = "test!1234";

        SignupRequest signUpRequest = new SignupRequest();

        signUpRequest.setEmail(email);
        signUpRequest.setLastName("Tester");
        signUpRequest.setFirstName("Tester");
        signUpRequest.setPassword(password);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .firstName("Tester")
                .lastName("Tester")
                .username(email)
                .password(password)
                .build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));


        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value(email))
                .andExpect(jsonPath("$.firstName").value("Tester"))
                .andExpect(jsonPath("$.lastName").value("Tester"))
                .andExpect(jsonPath("$.admin").value(false));
    }
}