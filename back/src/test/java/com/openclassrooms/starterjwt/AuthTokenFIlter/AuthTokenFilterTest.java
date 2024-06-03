package com.openclassrooms.starterjwt.AuthTokenFIlter;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.mockito.Mockito.*;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsServiceImpl;

public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testDoFilterInternal_InvalidToken() throws Exception {
        // Préparation des mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtUtils.validateJwtToken(anyString())).thenReturn(false);

        // Exécution de la méthode à tester
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérification que la méthode doFilter de FilterChain a été appelée
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_UserNotFound() throws Exception {
        // Préparation des mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("unknown_user");
        when(userDetailsService.loadUserByUsername("unknown_user")).thenThrow(new UsernameNotFoundException("User not found"));

        // Exécution de la méthode à tester
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérification que la méthode doFilter de FilterChain a été appelée
        verify(filterChain).doFilter(request, response);
    }
}


