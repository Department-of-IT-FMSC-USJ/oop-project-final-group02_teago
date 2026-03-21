package com.teago.teago.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.json",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/static/**",
                        "/api/auth/**",
                        "/api/delivery-options/**",
                        "/api/deliveries/**",
                        "/api/loans/**",
                        "/api/fertilizer/**",
                        "/api/cheques/**",
                        "/api/complaints/**",
                        "/api/employees/**",
                        "/api/payments/**",
                        "/api/bills/**",
                        "/api/factories/**"
                ).permitAll()
                .anyRequest().permitAll()  // Allow all other requests without authentication for now
            )
            .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}