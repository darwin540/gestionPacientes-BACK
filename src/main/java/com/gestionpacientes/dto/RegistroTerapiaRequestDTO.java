package com.gestionpacientes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTerapiaRequestDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "Debe proporcionar al menos un registro")
    @Size(min = 1, message = "Debe proporcionar al menos un registro")
    @Valid
    private List<RegistroTerapiaItemDTO> registros;
}

