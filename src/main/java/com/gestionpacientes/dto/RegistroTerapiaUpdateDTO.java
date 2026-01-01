package com.gestionpacientes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTerapiaUpdateDTO {

    private LocalDate fecha;
    private String servicioAbreviatura;

    @Min(value = 1, message = "El número de sesiones debe ser mínimo 1")
    @Max(value = 2, message = "El número de sesiones debe ser máximo 2")
    private Integer numeroSesiones;
}

