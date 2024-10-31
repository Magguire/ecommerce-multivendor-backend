package com.eshop.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean // Bean methods are automatically managed by the spring application
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management->management.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        )).authorizeRequests(authorize->authorize
                .requestMatchers("/api/**").authenticated()
                // Users must be authenticated with JET Token to access urls that start with /api
                .requestMatchers("/api/products/*/reviews").permitAll()
                // Users do not need to be authenticated to access reviews
                .anyRequest().permitAll()
                // Permit any other request whose form is other than the above
        ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf-> csrf.disable())
                .cors(cors->cors.configurationSource(corsConfigurationSource())); // Handles cors exception from frontend

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Collections.singletonList("*")); // All urls are accessible in frontend
                cfg.setAllowedMethods(Collections.singletonList("*")); // All methods(Get, Post, ...) are available in frontend
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setExposedHeaders(Collections.singletonList("Authorization"));
                cfg.setMaxAge(3600l);

                return cfg;
            }
        };
    };

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
