package com.gestionpacientes.config;

import com.gestionpacientes.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso público al login
                .requestMatchers("/api/auth/**").permitAll()
                
                // Endpoints que requieren rol ADMIN
                .requestMatchers("/api/profesionales/**").hasRole("ADMIN")
                .requestMatchers("/api/tipos-documento/activos").permitAll() // Permitir ver tipos activos sin autenticación
                .requestMatchers("/api/tipos-documento/**").hasRole("ADMIN") // Solo ADMIN puede gestionar tipos de documento
                .requestMatchers("/api/servicios-departamentos/activos").permitAll() // Permitir ver servicios activos sin autenticación
                .requestMatchers("/api/servicios-departamentos/**").hasRole("ADMIN") // Solo ADMIN puede gestionar servicios/departamentos
                .requestMatchers("/api/tipos-terapia/activos").permitAll() // Permitir ver tipos de terapia activos sin autenticación
                .requestMatchers("/api/tipos-terapia/**").hasRole("ADMIN") // Solo ADMIN puede gestionar tipos de terapia
                .requestMatchers("/api/pacientes/**").hasAnyRole("ADMIN", "PROFESIONAL")
                .requestMatchers("/api/terapias/**").hasAnyRole("ADMIN", "PROFESIONAL")
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(formLogin -> formLogin.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

