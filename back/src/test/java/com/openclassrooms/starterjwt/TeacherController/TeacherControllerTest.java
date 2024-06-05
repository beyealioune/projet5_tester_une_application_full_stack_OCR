package com.openclassrooms.starterjwt.TeacherController;//package com.openclassrooms.starterjwt.TeacherController;


    import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.mockito.Mockito.mock;
    import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

    @SpringBootTest
    @AutoConfigureMockMvc
    @ExtendWith(MockitoExtension.class)
    public class TeacherControllerTest{

        private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);


        @InjectMocks
        private TeacherController teacherController;


        @Mock
        private TeacherService teacherService;


        @Mock
        private TeacherMapper teacherMapper;

        @BeforeEach
        void setUp() {
            // Initialize the controller with mock dependencies
            teacherController = new TeacherController(teacherService, teacherMapper);
        }



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

        @Test
        @Tag("get_api/teacher")
        public void getAllTeachersShouldReturnAllTheTeachers() {
            // * Arrange

            Teacher mockTeacher1 = new Teacher();
            mockTeacher1.setId(1L);
            mockTeacher1.setFirstName("John");
            mockTeacher1.setLastName("Doe");
            mockTeacher1.setCreatedAt(LocalDateTime.now());
            mockTeacher1.setUpdatedAt(LocalDateTime.now());

            Teacher mockTeacher2 = new Teacher();
            mockTeacher2.setId(2L);
            mockTeacher2.setFirstName("Jane");
            mockTeacher2.setLastName("Doe");
            mockTeacher2.setCreatedAt(LocalDateTime.now());
            mockTeacher2.setUpdatedAt(LocalDateTime.now());

            List<Teacher> mockTeachers = Arrays.asList(
                    mockTeacher1,
                    mockTeacher2);

            when(teacherService.findAll()).thenReturn(mockTeachers);

            // Create an array to hold TeacherDto objects
            List<TeacherDto> expectedTeacherDtos = new ArrayList<>();

            // Iterate over the teachers and create corresponding TeacherDto objects
            for (int i = 0; i < mockTeachers.size(); i++) {
                Teacher teacher = mockTeachers.get(i);
                TeacherDto teacherDto = new TeacherDto();
                teacherDto.setId(teacher.getId());
                teacherDto.setFirstName(teacher.getFirstName());
                teacherDto.setLastName(teacher.getLastName());
                teacherDto.setCreatedAt(teacher.getCreatedAt());
                teacherDto.setUpdatedAt(teacher.getUpdatedAt());

                // Add the created TeacherDto to the array
                expectedTeacherDtos.add(teacherDto);
            }

            when(teacherMapper.toDto(mockTeachers)).thenReturn(expectedTeacherDtos); // You can create a mock TeacherDto as
            // needed

            // * Act
            ResponseEntity<?> result = teacherController.findAll();

            // * Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());

            Boolean hasSameArrayLength = ((List<Teacher>) result.getBody()).size() == mockTeachers.size();
            assertTrue(hasSameArrayLength);
        }


        @Test
        @Tag("get_api/teacher/{id}")
        public void getTeacherWithValidIdShouldReturnTheTeacher() {
            // * Arrange
            Long teacherId = 1L;
            Teacher mockTeacher = new Teacher();
            mockTeacher.setId(teacherId);
            mockTeacher.setFirstName("John");
            mockTeacher.setLastName("Doe");

            TeacherDto mockTeacherDto = new TeacherDto();
            mockTeacherDto.setId(mockTeacher.getId());
            mockTeacherDto.setFirstName(mockTeacher.getFirstName());
            mockTeacherDto.setLastName(mockTeacher.getLastName());

            when(teacherService.findById(teacherId)).thenReturn(mockTeacher);
            when(teacherMapper.toDto(mockTeacher)).thenReturn(mockTeacherDto);

            // * Act
            ResponseEntity<?> result = teacherController.findById(teacherId.toString());

            // * Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
        }


        @Test
        @Tag("get_api/teacher/{id}")
        public void getTeacherWithNonExistentIdShouldReturnANotFoundError() {
            // * Arrange
            Long teacherId = 0L;
            Teacher mockTeacher = new Teacher();
            mockTeacher.setId(teacherId);
            mockTeacher.setFirstName("John");
            mockTeacher.setLastName("Doe");

            when(teacherService.findById(teacherId)).thenReturn(null);

            // * Act
            ResponseEntity<?> result = teacherController.findById(teacherId.toString());

            // * Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }

        @Test
        @Tag("get_api/teacher/{id}")
        public void getTeacherWithInvalidIdShouldReturnABadRequestError() {
            // * Act
            ResponseEntity<?> result = teacherController.findById("invalid");

            // * Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }

    }



