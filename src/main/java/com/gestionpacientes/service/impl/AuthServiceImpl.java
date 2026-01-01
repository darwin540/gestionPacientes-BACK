package com.gestionpacientes.service.impl;

import com.gestionpacientes.dto.LoginRequestDTO;
import com.gestionpacientes.dto.LoginResponseDTO;
import com.gestionpacientes.entity.Profesional;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.repository.ProfesionalRepository;
import com.gestionpacientes.security.JwtUtil;
import com.gestionpacientes.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final ProfesionalRepository profesionalRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Profesional profesional = profesionalRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), profesional.getPassword())) {
            throw new ResourceNotFoundException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(profesional.getUsername(), profesional.getId());

        return LoginResponseDTO.builder()
                .token(token)
                .profesionalId(profesional.getId())
                .nombreProfesional(profesional.getNombre())
                .email(profesional.getEmail())
                .build();
    }
}

