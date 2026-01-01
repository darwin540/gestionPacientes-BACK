package com.gestionpacientes.mapper;

import com.gestionpacientes.dto.ServicioRequestDTO;
import com.gestionpacientes.dto.ServicioResponseDTO;
import com.gestionpacientes.dto.ServicioUpdateDTO;
import com.gestionpacientes.entity.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public ServicioResponseDTO toResponseDTO(Servicio servicio) {
        if (servicio == null) {
            return null;
        }

        return ServicioResponseDTO.builder()
                .id(servicio.getId())
                .nombreCompleto(servicio.getNombreCompleto())
                .abreviatura(servicio.getAbreviatura())
                .fechaCreacion(servicio.getFechaCreacion())
                .fechaActualizacion(servicio.getFechaActualizacion())
                .build();
    }

    public Servicio toEntity(ServicioRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Servicio.builder()
                .nombreCompleto(requestDTO.getNombreCompleto())
                .abreviatura(requestDTO.getAbreviatura())
                .build();
    }

    public void updateEntityFromDTO(ServicioUpdateDTO updateDTO, Servicio servicio) {
        if (updateDTO == null || servicio == null) {
            return;
        }

        if (updateDTO.getNombreCompleto() != null) {
            servicio.setNombreCompleto(updateDTO.getNombreCompleto());
        }
        if (updateDTO.getAbreviatura() != null) {
            servicio.setAbreviatura(updateDTO.getAbreviatura());
        }
    }
}

