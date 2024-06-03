//package com.openclassrooms.starterjwt.SessionControllerTest;
//
//import com.openclassrooms.starterjwt.controllers.SessionController;
//import com.openclassrooms.starterjwt.mapper.SessionMapper;
//import com.openclassrooms.starterjwt.services.SessionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
//import org.springframework.http.MediaType;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = SessionController.class)
//@ExtendWith(MockitoExtension.class)
//public class SessionControllerTest {
//
//    @Mock
//    private SessionService sessionService;
//
//    @Mock
//    private SessionMapper sessionMapper;
//
//    @InjectMocks
//    private SessionController sessionController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        // This method will be run before each test to initialize the mocks
//        MockitoAnnotations.openMocks(this);
//    }
//    @Test
//    void testFindById_NonExistentId() throws Exception {
//        when(sessionService.getById(anyLong())).thenReturn(null);
//
//        mockMvc.perform(get("/api/session/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }

