package com.gestionpacientes.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTerapiaUpdateDTO {

    private String nombre;

    @DecimalMin(value = "0.0", inclusive = false, message = "El valor unitario debe ser mayor que cero")
    private BigDecimal valorUnitario;
}

