package com.openclassrooms.starterjwt.SessionServiceTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;


    @Test
    void testCreate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Ensure the path is correct and the file exists
        InputStream inputStreamSession = getClass().getClassLoader().getResourceAsStream("MockSession/MockSession.json");
        assertNotNull(inputStreamSession, "The input stream should not be null. Check the file path.");

        Session session = mapper.readValue(inputStreamSession, new TypeReference<Session>() {});        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertNotNull(createdSession);
        assertEquals("Yoga", createdSession.getName());
        verify(sessionRepository, times(1)).save(session);
    }
    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(sessionRepository).deleteById(id);

        sessionService.delete(id);

        verify(sessionRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAll() {
        Session session1 = new Session().setName("Yoga").setDate(new Date()).setDescription("Yoga session");
        Session session2 = new Session().setName("Pilates").setDate(new Date()).setDescription("Pilates session");
        List<Session> sessions = Arrays.asList(session1, session2);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        assertEquals(2, foundSessions.size());
        verify(sessionRepository, times(1)).findAll();
    }
    @Test
    void testGetById() {
        Long id = 1L;
        Session session = new Session();
        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(id);

        assertNotNull(foundSession);
        verify(sessionRepository, times(1)).findById(id);
    }

    @Test
    void testGetByIdNotFound() {
        Long id = 1L;
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        Session foundSession = sessionService.getById(id);

        assertNull(foundSession);
        verify(sessionRepository, times(1)).findById(id);
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        Session session = new Session();
        session.setId(id);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session updatedSession = sessionService.update(id, session);

        assertNotNull(updatedSession);
        assertEquals(id, updatedSession.getId());
        verify(sessionRepository, times(1)).save(session);
    }


    @Test
    void testParticipate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        InputStream inputStreamSession = getClass().getClassLoader().getResourceAsStream("MockSession/MockSession.json");
        assertNotNull(inputStreamSession, "The input stream should not be null. Check the file path.");
        Session session = mapper.readValue(inputStreamSession, new TypeReference<Session>() {});

        User user = new User();
        user.setId(1L);

        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        sessionService.participate(1L, 1L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        InputStream inputStreamSession = getClass().getClassLoader().getResourceAsStream("MockSession/MockSession.json");
        assertNotNull(inputStreamSession, "The input stream should not be null. Check the file path.");
        Session session = mapper.readValue(inputStreamSession, new TypeReference<Session>() {});

        User user = new User();
        user.setId(1L);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        session.setUsers(userList);

        SessionRepository sessionRepository = mock(SessionRepository.class);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        SessionService sessionService = new SessionService(sessionRepository, null);

        sessionService.noLongerParticipate(1L, 1L);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipateSessionNotFound() throws IOException {
        Long sessionId = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testParticipateUserNotFound() throws IOException {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testParticipateUserAlreadyParticipating() throws IOException {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        User user = new User();
        user.setId(userId);
        session.setUsers(Collections.singletonList(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }
}
