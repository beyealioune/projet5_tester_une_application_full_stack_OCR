package com.openclassrooms.starterjwt.AuthTokenFIlter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {


    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private UserDetailsServiceImpl userDetailsService;


    @Test
    @Tag("AuthTokenFilter.doFilterInternal()")
    void doFilterInternalValidTokenShouldSetAuthenticationTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }


    @Test
    @Tag("AuthTokenFilter.doFilterInternal()")
    void doFilterInternalInvalidTokenShouldNotSetAuthenticationTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertEquals(SecurityContextHolder.getContext().getAuthentication(), null);
        verify(filterChain).doFilter(request, response);
    }


    @Test
    @Tag("AuthTokenFilter.parseJwt()")
    void parseJwtValidHeaderShouldReturnTokenTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");

        String result = authTokenFilter.parseJwt(request);

        assertEquals("testToken", result);
    }


    @Test
    @Tag("AuthTokenFilter.parseJwt()")
    void parseJwt_invalidHeader_shouldReturnNullTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String result = authTokenFilter.parseJwt(request);

        assertEquals(null, result);
    }

}



