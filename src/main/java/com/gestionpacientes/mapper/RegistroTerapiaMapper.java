package com.gestionpacientes.mapper;

import com.gestionpacientes.dto.RegistroTerapiaResponseDTO;
import com.gestionpacientes.dto.RegistroTerapiaUpdateDTO;
import com.gestionpacientes.entity.RegistroTerapiaServicio;
import org.springframework.stereotype.Component;

@Component
public class RegistroTerapiaMapper {

    public RegistroTerapiaResponseDTO toResponseDTO(RegistroTerapiaServicio registro) {
        if (registro == null) {
            return null;
        }

        return RegistroTerapiaResponseDTO.builder()
                .id(registro.getId())
                .pacienteId(registro.getPaciente().getId())
                .pacienteNombre(registro.getPaciente().getNombre())
                .pacienteApellido(registro.getPaciente().getApellido())
                .profesionalId(registro.getProfesional().getId())
                .profesionalNombre(registro.getProfesional().getNombre())
                .tipoTerapiaId(registro.getTipoTerapia().getId())
                .tipoTerapiaNombre(registro.getTipoTerapia().getNombre())
                .servicioId(registro.getServicio().getId())
                .servicioNombreCompleto(registro.getServicio().getNombreCompleto())
                .servicioAbreviatura(registro.getServicio().getAbreviatura())
                .fecha(registro.getFecha())
                .numeroSesiones(registro.getNumeroSesiones())
                .fechaCreacion(registro.getFechaCreacion())
                .fechaActualizacion(registro.getFechaActualizacion())
                .build();
    }

    public void updateEntityFromDTO(RegistroTerapiaUpdateDTO updateDTO, RegistroTerapiaServicio registro) {
        if (updateDTO == null || registro == null) {
            return;
        }

        if (updateDTO.getFecha() != null) {
            registro.setFecha(updateDTO.getFecha());
        }
        if (updateDTO.getNumeroSesiones() != null) {
            registro.setNumeroSesiones(updateDTO.getNumeroSesiones());
        }
    }
}


