package com.gestionpacientes.security;

import com.gestionpacientes.entity.Profesional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ProfesionalUserDetails implements UserDetails {

    private final Profesional profesional;

    public ProfesionalUserDetails(Profesional profesional) {
        this.profesional = profesional;
    }

    public Long getProfesionalId() {
        return profesional.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PROFESIONAL"));
    }

    @Override
    public String getPassword() {
        return profesional.getPassword();
    }

    @Override
    public String getUsername() {
        return profesional.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

