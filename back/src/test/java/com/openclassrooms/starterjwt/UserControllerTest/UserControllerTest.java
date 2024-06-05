package com.openclassrooms.starterjwt.UserControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;


    @SpringBootTest
    @ExtendWith(MockitoExtension.class)
    public class UserControllerTest {

        private static final Logger logger = LoggerFactory.getLogger(UserController.class);

        @InjectMocks
        private UserController userController;

        @Mock
        private UserRepository userRepository;

        @MockBean
        private UserService userService;


        @MockBean
        private UserMapper userMapper;


        @BeforeEach
        void setUp() {
            // Initialize the controller with mock dependencies
            userController = new UserController(userService, userMapper);
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
        @Test
        @Tag("get_api/user/{id}")
        public void getUserById_shouldReturnUserWithGivenId() {
            // * Arrange
            User mockUser = new User();
            mockUser.setId(1L);
            mockUser.setEmail("test@example.com");

            when(userService.findById(1L)).thenReturn(mockUser);
            when(userMapper.toDto(mockUser)).thenReturn(new UserDto());

            // * Act
            ResponseEntity<?> result = userController.findById("1");

            // * Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
        }


        @Test
        @Tag("get_api/user/{id}")
        public void getUserWithInvalidId_shouldReturnNotFoundError() {
            // * Assert
            Long nonExistentId = 0L;

            when(userService.findById(nonExistentId)).thenReturn(null);

            // * Act
            ResponseEntity<?> result = userController.findById(nonExistentId.toString());

            // * Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }

        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithValidId_shouldReturnOk() {
            // * Arrange
            Long userId = 1L;
            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setEmail("test@example.com");

            when(userService.findById(1L)).thenReturn(mockUser);

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("test@example.com");
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

            // * Act
            ResponseEntity<?> result = userController.save(userId.toString());

            // * Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
        }


        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithUnauthorizedUser_shouldReturnUnauthorized() {
            // * Arrange
            Long userId = 1L;
            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setEmail("test@example.com");

            when(userService.findById(1L)).thenReturn(mockUser);

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("unauthorized@example.com");
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

            // * Act
            ResponseEntity<?> result = userController.save(userId.toString());

            // * Assert
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }


        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithNonExistentId_shouldReturnNotFoundError() {
            // * Arrange
            Long nonExistentId = 0L;
            when(userService.findById(anyLong())).thenReturn(null);

            // * Act
            ResponseEntity<?> result = userController.save(nonExistentId.toString());

            // * Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }


        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithInvalidId_shouldReturnNotFoundError() {
            // * Act
            ResponseEntity<?> result = userController.save("invalid");

            // * Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
}
