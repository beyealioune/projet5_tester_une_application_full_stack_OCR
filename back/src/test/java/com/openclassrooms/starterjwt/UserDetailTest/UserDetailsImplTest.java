package com.openclassrooms.starterjwt.UserDetailTest;


import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @Test
    void testGetAuthorities() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testIsAccountNonExpired() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act + Assert
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act + Assert
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act + Assert
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act + Assert
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEquals_SameInstance() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        // Act + Assert
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    void testEquals_Null() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        // Act + Assert
        assertFalse(userDetails.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        // Act + Assert
        assertFalse(userDetails.equals("Not a UserDetailsImpl object"));
    }

    @Test
    void testEquals_DifferentId() {
        // Arrange
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(2L).build();

        // Act + Assert
        assertFalse(userDetails1.equals(userDetails2));
    }

    @Test
    void testEquals_SameId() {
        // Arrange
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(1L).build();

        // Act + Assert
        assertTrue(userDetails1.equals(userDetails2));
    }
}
