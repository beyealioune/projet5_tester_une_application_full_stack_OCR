package com.openclassrooms.starterjwt.UserDetailTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Optional;
import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }


    @Test
    @Tag("UserDetailsServiceImpl.LoadUserByUsername")
    void loadUserByUsernameValidUsernameTest() {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("testuser@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("testPassword");

        when(userRepository.findByEmail("testuser@test.com")).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser@test.com");

        // then
        assertNotNull(userDetails);
        assertEquals("testuser@test.com", userDetails.getUsername());
        assertEquals("John", ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals("Doe", ((UserDetailsImpl) userDetails).getLastName());
        assertEquals("testPassword", userDetails.getPassword());
    }


    @Test
    @Tag("UserDetailsServiceImpl.LoadUserByUsername")
    void loadUserByUsernameInvalidUsernameTest() {
        // * Arrange
        when(userRepository.findByEmail("nonexistentuser@test.com")).thenReturn(Optional.empty());

        // * Act
        // * Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistentuser@test.com"));
    }

}
