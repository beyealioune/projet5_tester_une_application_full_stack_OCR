package com.openclassrooms.starterjwt.SessionControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Session controller test: api/session")
public class SessionTestControllers {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    static private Instant startedAt;

    @BeforeAll
    static public void initStarting() {
        logger.info("Before all the test suites");
        startedAt = Instant.now();
    }

    @AfterAll
    static public void howManyTimeTest() {
        logger.info("After all the test suites");
        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();
        logger.info(MessageFormat.format("Duration of the tests : {0} ms", duration));
    }

    public SessionTestControllers(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Tag("get_api/session/{id}")
    public void getSessionByIdSessionWithGivenIdTest() {
        try {

            ResultActions result = mockMvc.perform(
                    get("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON));
            result.andExpect(status().isOk());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    @Tag("get_api/session/{id}")
    public void getSessionByIdWithInvalidIdBadRequestTest() {
        try {

            ResultActions result = mockMvc.perform(
                    get("/api/session/invalid")
                            .contentType(MediaType.APPLICATION_JSON));
            result.andExpect(status().isBadRequest());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Test
    @Tag("get_api/session/{id}")
    public void getSessionByIdWithNonExistentIdBadRequestTest() {
        try {

            ResultActions result = mockMvc.perform(
                    get("/api/session/0")
                            .contentType(MediaType.APPLICATION_JSON));
            result.andExpect(status().isNotFound());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Test
    @Tag("get_api/session")
    public void getAllSessionsListOfAllSessionsTest() {
        try {

            ResultActions result = mockMvc.perform(
                    get("/api/session/")
                            .contentType(MediaType.APPLICATION_JSON));
            result.andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    @Tag("post_api/session")
    public void createSessionWithValidSessionDtoCreatesNewSessionTest() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String isoString = "2023-12-30T10:27:21";

            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

            LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

            Teacher teacher = new Teacher();
            teacher
                    .setId(1L)
                    .setLastName("DELAHAYE")
                    .setFirstName("Margot")
                    .setCreatedAt(localDateTime)
                    .setUpdatedAt(localDateTime);

            Session session = Session.builder()
                    .id(1L)
                    .name("session-1")
                    .teacher(teacher)
                    .users(null)
                    .description("My description for the test")
                    .date(new Date())
                    .build();

            List<Long> userIdsList = new ArrayList<Long>();
            userIdsList.add(1L);
            userIdsList.add(2L);

            SessionDto sessionDto = new SessionDto();
            sessionDto.setId(session.getId());
            sessionDto.setTeacher_id(session.getTeacher().getId());
            sessionDto.setName(session.getName());
            sessionDto.setUsers(userIdsList);
            sessionDto.setDate(session.getDate());

            sessionDto.setDescription(session.getDescription());
            sessionDto.setCreatedAt(session.getCreatedAt());
            sessionDto.setUpdatedAt(session.getUpdatedAt());

            ResultActions result = mockMvc.perform(post("/api/session/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            result.andExpect(status().isOk());
        } catch (

                JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    @Tag("post_api/session")
    public void createSessionWithInvalidSessionDtoCreatesNewSessionTest() {
        try {
            SessionDto sessionDto = new SessionDto();

            ResultActions result = mockMvc.perform(post("/api/session/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            result.andExpect(status().isBadRequest());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("put_api/session/{id}")
    public void updateSession_withValidIdUpdatedSessionTest() {
        try {
            String isoString = "2023-12-30T10:27:21";

            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

            LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

            Teacher teacher = new Teacher();
            teacher
                    .setId(1L)
                    .setLastName("DELAHAYE")
                    .setFirstName("Margot")
                    .setCreatedAt(localDateTime)
                    .setUpdatedAt(localDateTime);

            Session session = Session.builder()
                    .id(1L)
                    .name("updated-session-1")
                    .teacher(teacher)
                    .users(null)
                    .description("My updated description for the test")
                    .date(new Date())
                    .build();

            List<Long> userIdsList = new ArrayList<Long>();
            userIdsList.add(1L);

            SessionDto sessionDto = new SessionDto();
            sessionDto.setId(session.getId());
            sessionDto.setTeacher_id(session.getTeacher().getId());
            sessionDto.setName(session.getName());
            sessionDto.setUsers(userIdsList);
            sessionDto.setDate(session.getDate());
            sessionDto.setDescription(session.getDescription());
            sessionDto.setCreatedAt(session.getCreatedAt());
            sessionDto.setUpdatedAt(session.getUpdatedAt());

            ResultActions result = mockMvc.perform(put("/api/session/" + sessionDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            result.andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    @Tag("put_api/session/{id}")
    public void updateSession_withInvalidIdBadRequestTest() {
        try {

            ResultActions result = mockMvc.perform(put("/api/session/1")
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isBadRequest());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    @Tag("delete_api/session/{id}")
        @DisplayName("(HAPPY PATH) it should delete the session and return a 200 status code")
        public void deleteSessionWithValidIdBadRequestTest () {
            try {

                ResultActions result = mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON));


                result.andExpect(status().isOk());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // Assert
        @Test
        @Tag("delete_api/session/{id}")
        public void deleteSessionWithNonExistantIdBadRequestTest() {
            try {
                // Arrange
                // Act
                ResultActions result = mockMvc.perform(delete("/api/session/0")
                        .contentType(MediaType.APPLICATION_JSON));
                // Assert
                result.andExpect(status().isNotFound());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    @Test
    @Tag("delete_api/session/{id}")
    public void deleteSessionWithInvalidIdBadRequestTest() {
        try {

            ResultActions result = mockMvc.perform(delete("/api/session/invalid")
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isBadRequest());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithValidIdsRemoveTheUserFromSessionTest() {

        try {

            ResultActions result = mockMvc.perform(delete("/api/session/1/participate/0")
                    .contentType(MediaType.APPLICATION_JSON));
            result.andExpect(status().isOk());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithInvalidIdsRemoveTheUserFromSessionTest() {

            try {

                ResultActions result = mockMvc.perform(delete("/api/session/1/participate/0")
                        .contentType(MediaType.APPLICATION_JSON));


                result.andExpect(status().isNotFound());
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


