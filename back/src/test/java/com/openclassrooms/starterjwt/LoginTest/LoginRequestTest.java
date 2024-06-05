package com.openclassrooms.starterjwt.LoginTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginRequestTest {

    @Test
    public void testGetEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        assertEquals("test@example.com", loginRequest.getEmail());
    }

    @Test
    public void testGetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");
        assertEquals("password", loginRequest.getPassword());
    }

    @Test
    public void testSetEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        assertEquals("test@example.com", loginRequest.getEmail());
    }

    @Test
    public void testSetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");
        assertEquals("password", loginRequest.getPassword());
    }
}
