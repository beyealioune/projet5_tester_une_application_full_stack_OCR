package com.openclassrooms.starterjwt.UserDetailTest;


import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getId(), ((UserDetailsImpl) userDetails).getId());
        assertEquals(email, userDetails.getUsername());
        assertEquals(user.getFirstName(), ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals(user.getLastName(), ((UserDetailsImpl) userDetails).getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));

        verify(userRepository, times(1)).findByEmail(email);
    }
}
