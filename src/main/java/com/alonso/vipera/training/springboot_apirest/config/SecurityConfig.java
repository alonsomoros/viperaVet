package com.alonso.vipera.training.springboot_apirest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Configuración de seguridad para la aplicación Spring Boot.
 * Define las reglas de seguridad, los endpoints públicos y la integración
 * del filtro JWT.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtFilter;
        private final AuthenticationProvider authenticationProvider;

        /**
         * Configura la cadena de filtros de seguridad HTTP.
         * Define los endpoints públicos, las políticas de sesión y añade el filtro JWT.
         * 
         * @param http HttpSecurity
         * @return SecurityFilterChain
         * @throws Exception excepción en caso de error
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/auth/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/v3/api-docs.yaml",
                                                                "/swagger-resources/**",
                                                                "/webjars/**",
                                                                "/configuration/ui",
                                                                "/configuration/security",
                                                                "/api/dog-breeds",
                                                                "/api/dog-breeds/save-all",
                                                                "/api/cat-breeds",
                                                                "/api/cat-breeds/save-all",
                                                                "/breeds/**",
                                                                "/species/**",
                                                                "/prueba/**",
                                                                "/pets/**")
                                                .permitAll() // Endpoints de autenticación son públicos
                                                .anyRequest().authenticated() // Todos los demás requieren autenticación
                                )
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
