package com.gestionpacientes.mapper;

import com.gestionpacientes.dto.PacienteRequestDTO;
import com.gestionpacientes.dto.PacienteResponseDTO;
import com.gestionpacientes.dto.PacienteUpdateDTO;
import com.gestionpacientes.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public PacienteResponseDTO toResponseDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        return PacienteResponseDTO.builder()
                .id(paciente.getId())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .tipoDocumento(paciente.getTipoDocumento())
                .documento(paciente.getDocumento())
                .fechaCreacion(paciente.getFechaCreacion())
                .fechaActualizacion(paciente.getFechaActualizacion())
                .build();
    }

    public Paciente toEntity(PacienteRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Paciente.builder()
                .nombre(requestDTO.getNombre())
                .apellido(requestDTO.getApellido())
                .tipoDocumento(requestDTO.getTipoDocumento())
                .documento(requestDTO.getDocumento())
                .build();
    }

    public void updateEntityFromDTO(PacienteUpdateDTO updateDTO, Paciente paciente) {
        if (updateDTO == null || paciente == null) {
            return;
        }

        if (updateDTO.getNombre() != null) {
            paciente.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getApellido() != null) {
            paciente.setApellido(updateDTO.getApellido());
        }
        if (updateDTO.getTipoDocumento() != null) {
            paciente.setTipoDocumento(updateDTO.getTipoDocumento());
        }
        if (updateDTO.getDocumento() != null) {
            paciente.setDocumento(updateDTO.getDocumento());
        }
    }
}

