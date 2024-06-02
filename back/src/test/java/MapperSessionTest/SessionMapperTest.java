package MapperSessionTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;


import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Test
    public void testToEntity() {
        // Créer un objet SessionDto
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(List.of(1L, 2L, 3L));

        // Mock des méthodes findById de TeacherService et UserService
        when(teacherService.findById(1L)).thenReturn(new Teacher());
        when(userService.findById(1L)).thenReturn(new User());
        when(userService.findById(2L)).thenReturn(new User());
        when(userService.findById(3L)).thenReturn(new User());

        // Appel de la méthode à tester
        Session session = sessionMapper.toEntity(sessionDto);

        // Assertions
        assertEquals("Test description", session.getDescription());
        // Assurez-vous que les utilisateurs sont correctement mappés
        assertEquals(3, session.getUsers().size());
    }

    @Test
    public void testToDto() {
        // Créer un objet Session
        Session session = new Session();
        session.setDescription("Test description");
        session.setTeacher(new Teacher());
        session.setUsers(List.of(new User(), new User(), new User()));

        // Appel de la méthode à tester
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assertions
        assertEquals("Test description", sessionDto.getDescription());
        // Assurez-vous que les utilisateurs sont correctement mappés
        assertEquals(3, sessionDto.getUsers().size());
    }
}
