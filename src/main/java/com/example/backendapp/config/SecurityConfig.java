package com.example.backendapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.swing.undo.AbstractUndoableEdit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                }))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/calendar").authenticated()
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final var realmAccess = Optional.ofNullable((Map<String, Object>) jwt.getClaimAsMap("realm_access"));
            final var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.getOrDefault("roles", List.of())));
            return roles.map(List::stream).
                    orElse(Stream.empty())
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast).toList();
        });
        return converter;
    }

}
