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
        void findByIdTest() {
            // given
            long userId = 1L;
            User mockUser = new User();
            mockUser.setId(userId);

            when(userService.findById(userId)).thenReturn(mockUser);

            UserDto mockUserDto = new UserDto(); // Create a UserDto object
            when(userMapper.toDto(mockUser)).thenReturn(mockUserDto); // Return the UserDto object

            // when
            ResponseEntity<?> responseEntity = userController.findById(String.valueOf(userId));

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(mockUserDto, responseEntity.getBody());

            verify(userService, times(1)).findById(userId);
        }
        @Test
        void deleteTest() {
            // given
            long userId = 1L;
            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setEmail("test@example.com");

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("test@example.com");
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userService.findById(userId)).thenReturn(mockUser);

            // when
            ResponseEntity<?> responseEntity = userController.save(String.valueOf(userId));

            // then
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            // Verify that the userService.delete method was called
            verify(userService, times(1)).delete(userId);
        }
        @Test
        @Tag("get_api/user/{id}")
        public void getUserByIdReturnUserWithGivenIdTest() {
            //given
            User mockUser = new User();
            mockUser.setId(1L);
            mockUser.setEmail("test@example.com");

            when(userService.findById(1L)).thenReturn(mockUser);
            when(userMapper.toDto(mockUser)).thenReturn(new UserDto());

            // when
            ResponseEntity<?> result = userController.findById("1");

            // then
            assertEquals(HttpStatus.OK, result.getStatusCode());
        }


        @Test
        @Tag("get_api/user/{id}")
        public void getUserWithInvalidIddReturnNotFoundErrorTest() {
            Long nonExistentId = 0L;

            when(userService.findById(nonExistentId)).thenReturn(null);

            ResponseEntity<?> result = userController.findById(nonExistentId.toString());

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }

        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithValidIdReturnOkTest() {
            //given
            Long userId = 1L;
            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setEmail("test@example.com");

            when(userService.findById(1L)).thenReturn(mockUser);

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("test@example.com");
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

            //when
            ResponseEntity<?> result = userController.save(userId.toString());

            //then
            assertEquals(HttpStatus.OK, result.getStatusCode());
        }


        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithUnauthorizedUserReturnUnauthorizedTest() {
            // given
            Long userId = 1L;
            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setEmail("test@example.com");

            when(userService.findById(1L)).thenReturn(mockUser);

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("unauthorized@example.com");
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

            //when
            ResponseEntity<?> result = userController.save(userId.toString());

            // then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }


        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithNonExistentIdReturnNotFoundErrorTest() {
            Long nonExistentId = 0L;
            when(userService.findById(anyLong())).thenReturn(null);

            ResponseEntity<?> result = userController.save(nonExistentId.toString());

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }


        @Test
        @Tag("delete_api/user/{id}")
        public void deleteUserWithInvalidIdReturnNotFoundErrorTest() {
            ResponseEntity<?> result = userController.save("invalid");

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
}
