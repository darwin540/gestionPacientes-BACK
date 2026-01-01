package com.gestionpacientes.security;

import com.gestionpacientes.entity.Profesional;
import com.gestionpacientes.repository.ProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfesionalUserDetailsService implements UserDetailsService {

    private final ProfesionalRepository profesionalRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Profesional no encontrado con nombre de usuario: " + username));
        return new ProfesionalUserDetails(profesional);
    }
}

