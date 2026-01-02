package com.gestionpacientes.mapper;

import com.gestionpacientes.dto.TipoTerapiaRequestDTO;
import com.gestionpacientes.dto.TipoTerapiaResponseDTO;
import com.gestionpacientes.dto.TipoTerapiaUpdateDTO;
import com.gestionpacientes.entity.TipoTerapia;
import org.springframework.stereotype.Component;

@Component
public class TipoTerapiaMapper {

    public TipoTerapiaResponseDTO toResponseDTO(TipoTerapia tipoTerapia) {
        if (tipoTerapia == null) {
            return null;
        }

        return TipoTerapiaResponseDTO.builder()
                .id(tipoTerapia.getId())
                .nombre(tipoTerapia.getNombre())
                .valorUnitario(tipoTerapia.getValorUnitario())
                .fechaCreacion(tipoTerapia.getFechaCreacion())
                .fechaActualizacion(tipoTerapia.getFechaActualizacion())
                .build();
    }

    public TipoTerapia toEntity(TipoTerapiaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return TipoTerapia.builder()
                .nombre(requestDTO.getNombre())
                .valorUnitario(requestDTO.getValorUnitario())
                .build();
    }

    public void updateEntityFromDTO(TipoTerapiaUpdateDTO updateDTO, TipoTerapia tipoTerapia) {
        if (updateDTO == null || tipoTerapia == null) {
            return;
        }

        if (updateDTO.getNombre() != null) {
            tipoTerapia.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getValorUnitario() != null) {
            tipoTerapia.setValorUnitario(updateDTO.getValorUnitario());
        }
    }
}


