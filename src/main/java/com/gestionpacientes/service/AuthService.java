package com.gestionpacientes.service;

import com.gestionpacientes.dto.LoginRequestDTO;
import com.gestionpacientes.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequest);
}

