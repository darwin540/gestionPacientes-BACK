package com.gestionpacientes.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTerapiaRequestDTO {

    @NotBlank(message = "El nombre del tipo de terapia es obligatorio")
    private String nombre;

    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor unitario debe ser mayor que cero")
    private BigDecimal valorUnitario;
}

