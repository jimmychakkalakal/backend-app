package com.example.backendapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class CalendarController {

    @GetMapping("v1/calender")
    @PreAuthorize("hasAuthority('my-role')")
    public List<Map<String, Object>> getCalendar(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return List.of(
                Map.of("date", LocalDate.now().toString(), "event", "Team Standup", "user", username),
                Map.of("date", LocalDate.now().plusDays(1).toString(), "event", "Project Planning", "user", username),
                Map.of("date", LocalDate.now().plusDays(2).toString(), "event", "Code Review", "user", username)
        );
    }

}
