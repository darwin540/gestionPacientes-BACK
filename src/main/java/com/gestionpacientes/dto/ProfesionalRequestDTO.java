package com.gestionpacientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La profesión es obligatoria")
    private String profesion;

    @NotBlank(message = "El número de cuenta de banco es obligatorio")
    private String numeroCuentaBanco;

    @NotBlank(message = "El nombre del banco es obligatorio")
    private String nombreBanco;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Builder.Default
    private Set<Long> tiposTerapiaIds = new HashSet<>();
}

