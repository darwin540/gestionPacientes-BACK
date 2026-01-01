package com.gestionpacientes.dto;

import com.gestionpacientes.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteUpdateDTO {

    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private String documento;
}

