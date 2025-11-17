package com.example.backendapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CalendarController.class)
class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCalendarReturns401() throws Exception {
        mockMvc.perform(get("/api/calender")).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCalendarReturnsEvents() throws Exception {
        mockMvc.perform(
                        get("/api/calender")
                                .with(jwt().jwt(jwt -> {
                                    jwt.claim("preferred_username", "demo");
                                    jwt.claim("realm_access", Map.of("roles", java.util.List.of("my-role")));
                                }))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event").value("Team Standup"))
                .andExpect(jsonPath("$[0].user").value("demo"));
    }

    @Test
    void testGetCalendarReturnsForbidden() throws Exception {
        mockMvc.perform(
                        get("/api/calender")
                                .with(jwt()
                                        .jwt(jwt -> {
                                            jwt.header("alg", "none");
                                            jwt.claim("preferred_username", "demo");
                                            jwt.claim("realm_access",
                                                    Map.of("roles", List.of("other-role")));
                                        })
                                )
                )
                .andExpect(status().isForbidden());
    }
}