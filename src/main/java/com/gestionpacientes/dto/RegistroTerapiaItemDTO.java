package com.gestionpacientes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTerapiaItemDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La abreviatura del servicio es obligatoria")
    private String servicioAbreviatura;

    @NotNull(message = "El número de sesiones es obligatorio")
    @Min(value = 1, message = "El número de sesiones debe ser mínimo 1")
    @Max(value = 2, message = "El número de sesiones debe ser máximo 2")
    private Integer numeroSesiones;
}

