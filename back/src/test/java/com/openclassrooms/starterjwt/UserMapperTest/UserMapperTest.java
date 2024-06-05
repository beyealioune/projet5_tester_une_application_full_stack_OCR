package com.openclassrooms.starterjwt.UserMapperTest;

import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testToEntity() {
        // Créer un objet DTO avec tous les champs nécessaires, y compris le mot de passe
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setAdmin(true);
        userDto.setPassword("password123"); // Définir un mot de passe non null

        // Mapper le DTO vers une entité
        User user = userMapper.toEntity(userDto);

        // Assertions sur l'entité résultante
        assertNotNull(user);
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getPassword(), user.getPassword()); // Vérifier le mot de passe
    }


    @Test
    public void testToDto() {
        // Given
        User user = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, null, null);

        // When
        UserDto userDto = userMapper.toDto(user);

        // Then
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.isAdmin(), userDto.isAdmin());
    }
}
