package com.gestionpacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalUpdateDTO {

    private String nombre;
    private String profesion;
    private String numeroCuentaBanco;
    private String nombreBanco;
    private Set<Long> tiposTerapiaIds;
}

