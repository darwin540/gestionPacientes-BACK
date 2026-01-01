package com.gestionpacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    @Builder.Default
    private String tipoToken = "Bearer";
    private Long profesionalId;
    private String nombreProfesional;
    private String email;
}

