package com.openclassrooms.starterjwt.UserDetailTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @InjectMocks
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("testPassword")
                .build();
    }


    @Test
    @Tag("UserDetailsImpl.Construction")
    void testUserDetailsConstructionAndGetters() {
        assertEquals(1L, userDetails.getId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertFalse(userDetails.getAdmin());
        assertEquals("testPassword", userDetails.getPassword());
    }

    @Test
    @Tag("UserDetailsImpl.getAuthorities()")
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    @Tag("UserDetailsImpl.isAccountNonExpired()")
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }


    @Test
    @Tag("UserDetailsImpl.isAccountNonLocked()")
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }


    @Test
    @Tag("UserDetailsImpl.isCredentialsNonExpired()")
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }


    @Test
    @Tag("UserDetailsImpl.isEnabled()")
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @Tag("UserDetailsImpl.equals()")
    void testEqualsSameUser() {
        UserDetailsImpl sameUser = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("testPassword")
                .build();
        assertTrue(userDetails.equals(sameUser));
    }

    @Test
    @Tag("UserDetailsImpl.equals()")
    void testEqualsDifferentUser() {
        UserDetailsImpl differentUser = UserDetailsImpl.builder()
                .id(2L)
                .username("anotheruser")
                .firstName("Jane")
                .lastName("Doe")
                .admin(true)
                .password("differentPassword")
                .build();
        assertFalse(userDetails.equals(differentUser));
    }


    @Test
    @Tag("UserDetailsImpl.equals()")
    void testEqualsNonUserDetailsObject() {
        Object nonUserDetailsObject = new Object();
        assertFalse(userDetails.equals(nonUserDetailsObject));
    }


    @Test
    @Tag("UserDetailsImpl.equals()")
    void testEqualsWithNull() {
        assertFalse(userDetails.equals(null));
    }


    @Test
    @Tag("UserDetailsImpl.hashCode()")
    void testHashCode() {
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("testPassword")
                .build();

        assertNotEquals(userDetails.hashCode(), differentUserDetails.hashCode());
    }

}

