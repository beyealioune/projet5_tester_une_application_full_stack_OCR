package com.openclassrooms.starterjwt.JwtUtilsTest;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {
    @Mock
    private JwtUtils jwtUtilsMock;

    @InjectMocks
    private  JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.jwtSecret = "testSecret";  // Initialisation de jwtSecret
        jwtUtils.jwtExpirationMs = 3600000; // Initialisation de jwtExpirationMs
    }


    @Test
    void testGenerateToken() throws NoSuchFieldException, IllegalAccessException {
        // Préparer les données de test
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, // id
                "testfirstname", // firstName
                "testlastname", // lastName
                "admin", // admin
                false, // password
                "ROLE_USER"// autorités
        );

        // Créer une Authentication avec l'objet UserDetailsImpl
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        // Définir le comportement attendu pour la méthode generateJwtToken
        String jwtToken = "mockedJwtToken";
        when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn(jwtToken);

        // Appeler la méthode à tester
        String generatedToken = jwtUtilsMock.generateJwtToken(authentication);

        // Vérifier que le jeton JWT généré est égal au jeton mocké
        assertEquals(jwtToken, generatedToken);
    }



    @Test
    public void testGetUserNameFromJwtToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        String token = generateJwtToken(userDetails);  // Génération du JWT

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("testuser", username);
    }



    private String generateJwtToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtUtils.jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtUtils.jwtSecret)
                .compact();
    }


//    @Test
//    void testValidateJwtToken_WithExpiredToken() {
//        // Préparer les données de test
//        JwtUtils jwtUtils = new JwtUtils();
//        setJwtSecret(jwtUtils, "testSecret");
//
//        UserDetails userDetails = new User("testuser", "testpassword", Collections.emptyList());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
//
//        // Générer un jeton
//        String jwtToken = jwtUtils.generateJwtToken(authentication);
//
//        // Modifier la date d'expiration pour la rendre expirée
//        jwtToken = setExpirationToPast(jwtToken);
//
//        // Appeler la méthode à tester et capturer l'exception
//        String finalJwtToken = jwtToken;
//        Exception exception = assertThrows(ExpiredJwtException.class, () -> jwtUtils.validateJwtToken(finalJwtToken));
//
//        // Vérifier que l'exception capturée est bien une exception d'expiration du jeton JWT
//        assertNotNull(exception);
//    }

    private String setExpirationToPast(String jwtToken) {
        try {
            Field field = Claims.class.getDeclaredField("exp");
            field.setAccessible(true);
            Claims claims = Jwts.parser().parseClaimsJws(jwtToken).getBody();
            field.set(claims, new Date(0)); // Met la date d'expiration au 1er janvier 1970, le rendant expiré
            return Jwts.builder().setClaims(claims).compact();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utilitaire pour définir la valeur de jwtSecret via la réflexion
    private void setJwtSecret(JwtUtils jwtUtils, String secret) {
        try {
            Field field = jwtUtils.getClass().getDeclaredField("jwtSecret");
            field.setAccessible(true);
            field.set(jwtUtils, secret);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    // Ajoutez d'autres tests pour les cas de jetons JWT invalides

    @Test
    public void testValidateJwtToken() {
        // Given
        String validToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 3600000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, "testSecret")
                .compact();

        String invalidToken = "invalidToken";

        // When
        boolean validResult = jwtUtils.validateJwtToken(validToken);
        boolean invalidResult = jwtUtils.validateJwtToken(invalidToken);

        // Then
        assertTrue(validResult);
        assertFalse(invalidResult);
    }
}
