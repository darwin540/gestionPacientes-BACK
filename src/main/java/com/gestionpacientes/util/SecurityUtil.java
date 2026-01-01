package com.gestionpacientes.util;

import com.gestionpacientes.security.ProfesionalUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getProfesionalIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof ProfesionalUserDetails) {
            ProfesionalUserDetails userDetails = (ProfesionalUserDetails) authentication.getPrincipal();
            return userDetails.getProfesionalId();
        }
        throw new RuntimeException("No se pudo obtener el profesional autenticado");
    }
}

