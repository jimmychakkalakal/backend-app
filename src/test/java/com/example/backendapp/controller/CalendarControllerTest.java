package com.example.backendapp.controller;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithJwt;
import com.example.backendapp.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CalendarController.class)
@Import(SecurityConfig.class)
class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void testGetCalendarReturns401() throws Exception {
        mockMvc.perform(get("/api/v1/calender")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithJwt("demo.json")
    void testGetCalendarReturnsEvents() throws Exception {
        mockMvc.perform(get("/api/v1/calender")).
                andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user").value("demo"))
                .andExpect(jsonPath("$[0].event").value("Team Standup"));
    }

    @Test
    @WithJwt("test.json")
    void testGetCalendarReturns403() throws Exception {
        mockMvc.perform(get("/api/v1/calender")).andExpect(status().isForbidden());

    }
}