package com.gestionpacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTerapiaResponseDTO {

    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;
    private Long profesionalId;
    private String profesionalNombre;
    private Long tipoTerapiaId;
    private String tipoTerapiaNombre;
    private Long servicioId;
    private String servicioNombreCompleto;
    private String servicioAbreviatura;
    private LocalDate fecha;
    private Integer numeroSesiones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

