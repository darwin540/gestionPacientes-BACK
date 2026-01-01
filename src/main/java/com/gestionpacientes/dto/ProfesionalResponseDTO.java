package com.gestionpacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalResponseDTO {

    private Long id;
    private String nombre;
    private String profesion;
    private String numeroCuentaBanco;
    private String nombreBanco;
    private Set<TipoTerapiaResponseDTO> tiposTerapia;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

