package com.gestionpacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponseDTO {

    private Long id;
    private String nombreCompleto;
    private String abreviatura;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}


