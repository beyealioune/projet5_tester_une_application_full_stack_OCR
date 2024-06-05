package com.openclassrooms.starterjwt.EntityMapperTest;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.EntityMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private EntityMapper<UserDto, User> userMapper;

    @Test
    public void testToEntity() {
        // Given
        UserDto userDTO = new UserDto();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setLastName("Doe");
        userDTO.setFirstName("John");
        userDTO.setPassword("password");
        userDTO.setAdmin(true);

        // When
        User user = userMapper.toEntity(userDTO);

        // Then
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(userDTO.isAdmin(), user.isAdmin());
    }

    @Test
    public void testToDto() {
        // Given
        User user = new User(1L, "test@example.com", "Doe", "John", "password", true, null, null);

        // When
        UserDto userDTO = userMapper.toDto(user);

        // Then
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.isAdmin(), userDTO.isAdmin());
    }
}
