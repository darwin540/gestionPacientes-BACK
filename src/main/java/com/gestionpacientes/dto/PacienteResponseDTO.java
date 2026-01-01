package com.gestionpacientes.dto;

import com.gestionpacientes.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private String documento;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

