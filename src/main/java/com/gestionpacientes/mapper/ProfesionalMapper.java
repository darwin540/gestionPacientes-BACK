package com.gestionpacientes.mapper;

import com.gestionpacientes.dto.ProfesionalRequestDTO;
import com.gestionpacientes.dto.ProfesionalResponseDTO;
import com.gestionpacientes.dto.ProfesionalUpdateDTO;
import com.gestionpacientes.dto.TipoTerapiaResponseDTO;
import com.gestionpacientes.entity.Profesional;
import com.gestionpacientes.entity.TipoTerapia;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProfesionalMapper {

    private final TipoTerapiaMapper tipoTerapiaMapper;

    public ProfesionalMapper(TipoTerapiaMapper tipoTerapiaMapper) {
        this.tipoTerapiaMapper = tipoTerapiaMapper;
    }

    public ProfesionalResponseDTO toResponseDTO(Profesional profesional) {
        if (profesional == null) {
            return null;
        }

        Set<TipoTerapiaResponseDTO> tiposTerapiaDTO = profesional.getTiposTerapia() != null ?
                profesional.getTiposTerapia().stream()
                        .map(tipoTerapiaMapper::toResponseDTO)
                        .collect(Collectors.toSet()) :
                null;

        return ProfesionalResponseDTO.builder()
                .id(profesional.getId())
                .nombre(profesional.getNombre())
                .profesion(profesional.getProfesion())
                .numeroCuentaBanco(profesional.getNumeroCuentaBanco())
                .nombreBanco(profesional.getNombreBanco())
                .tiposTerapia(tiposTerapiaDTO)
                .fechaCreacion(profesional.getFechaCreacion())
                .fechaActualizacion(profesional.getFechaActualizacion())
                .build();
    }

    public Profesional toEntity(ProfesionalRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Profesional.builder()
                .nombre(requestDTO.getNombre())
                .profesion(requestDTO.getProfesion())
                .numeroCuentaBanco(requestDTO.getNumeroCuentaBanco())
                .nombreBanco(requestDTO.getNombreBanco())
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(requestDTO.getPassword()) // Se debe encriptar en el servicio
                .build();
    }

    public void updateEntityFromDTO(ProfesionalUpdateDTO updateDTO, Profesional profesional) {
        if (updateDTO == null || profesional == null) {
            return;
        }

        if (updateDTO.getNombre() != null) {
            profesional.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getProfesion() != null) {
            profesional.setProfesion(updateDTO.getProfesion());
        }
        if (updateDTO.getNumeroCuentaBanco() != null) {
            profesional.setNumeroCuentaBanco(updateDTO.getNumeroCuentaBanco());
        }
        if (updateDTO.getNombreBanco() != null) {
            profesional.setNombreBanco(updateDTO.getNombreBanco());
        }
    }

    public void asignarTiposTerapia(Profesional profesional, Set<TipoTerapia> tiposTerapia) {
        if (profesional != null && tiposTerapia != null) {
            profesional.getTiposTerapia().clear();
            profesional.getTiposTerapia().addAll(tiposTerapia);
        }
    }
}

