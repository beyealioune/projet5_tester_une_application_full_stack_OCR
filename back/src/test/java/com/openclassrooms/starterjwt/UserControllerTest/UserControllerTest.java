package com.openclassrooms.starterjwt.UserControllerTest;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindById() {
        // Mocking data
        long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        // Mocking service method
        when(userService.findById(userId)).thenReturn(mockUser);

        // Mocking mapper method
        UserDto mockUserDto = new UserDto(); // Create a UserDto object
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto); // Return the UserDto object

        // Call the controller method
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(userId));

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUserDto, responseEntity.getBody());

        // Verify that the userService.findById method was called
        verify(userService, times(1)).findById(userId);
    }
    @Test
    void testDelete() {
        // Mocking data
        long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        // Mocking authentication
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mocking service method
        when(userService.findById(userId)).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<?> responseEntity = userController.save(String.valueOf(userId));

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the userService.delete method was called
        verify(userService, times(1)).delete(userId);
    }
}
