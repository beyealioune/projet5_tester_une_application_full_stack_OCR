package com.openclassrooms.starterjwt.TeacherController;//package com.openclassrooms.starterjwt.TeacherController;
//
//
//import com.openclassrooms.starterjwt.controllers.TeacherController;
//import com.openclassrooms.starterjwt.dto.TeacherDto;
//import com.openclassrooms.starterjwt.mapper.TeacherMapper;
//import com.openclassrooms.starterjwt.models.Teacher;
//import com.openclassrooms.starterjwt.services.TeacherService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import static org.mockito.Mockito.when;
//
//@WebMvcTest(TeacherController.class)
//@ExtendWith({SpringExtension.class, MockitoExtension.class})
//public class TeacherControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TeacherService teacherService;
//
//    @MockBean
//    private TeacherMapper teacherMapper;
//
//    private Teacher teacher;
//
//    @BeforeEach
//    public void setUp() {
//        teacher = new Teacher();
//        teacher.setId(1L);
//        teacher.setLastName("John Doe");
//        // Autres attributs de l'enseignant...
//    }
//
//    @Test
//    public void testFindById_TeacherFound() throws Exception {
//        // Given
//        when(teacherService.findById(1L)).thenReturn(teacher);
//        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
//    }
//
//    @Test
//    public void testFindById_TeacherNotFound() throws Exception {
//        // Given
//        when(teacherService.findById(1L)).thenReturn(null);
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1"))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    @Test
//    public void testFindById_InvalidId() throws Exception {
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/invalid-id"))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    public void testFindAll() throws Exception {
//        // Given
//        List<Teacher> teachers = Collections.singletonList(teacher);
//        when(teacherService.findAll()).thenReturn(teachers);
//        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"));
//    }
//}

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TeacherControllerTest {

    @Test
    void testFindById() {
        // Mocking data
        long teacherId = 1L;
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(teacherId);

        // Mocking service method
        TeacherService teacherService = mock(TeacherService.class);
        when(teacherService.findById(teacherId)).thenReturn(mockTeacher);

        // Mocking mapper method
        TeacherMapper teacherMapper = mock(TeacherMapper.class);
        TeacherDto mockTeacherDto = new TeacherDto(); // Create a TeacherDto object
        when(teacherMapper.toDto(mockTeacher)).thenReturn(mockTeacherDto); // Return the TeacherDto object

        // Create the controller with mocked dependencies
        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);

        // Call the controller method
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(teacherId));

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockTeacherDto, responseEntity.getBody());
    }


}

