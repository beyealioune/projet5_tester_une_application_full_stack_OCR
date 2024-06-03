package com.openclassrooms.starterjwt.TeacherMapperTest;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testToEntity() {
        // Given
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");

        // When
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Then
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }

    @Test
    public void testToDto() {
        // Given
        Teacher teacher = new Teacher(1L, "Doe", "John", null, null);

        // When
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Then
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }
}
