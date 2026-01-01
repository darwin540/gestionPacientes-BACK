package com.gestionpacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTerapiaResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal valorUnitario;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

