package com.gestionpacientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProfesionalDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "La profesión es obligatoria")
    private String profesion;

    @NotBlank(message = "El tipo de terapia es obligatorio")
    private String tipoTerapia;

    @NotNull(message = "El valor por terapia es obligatorio")
    @Positive(message = "El valor por terapia debe ser positivo")
    private BigDecimal valorPorTerapia;

    // Constructores
    public ProfesionalDTO() {
    }

    public ProfesionalDTO(Long id, String nombre, String nombreUsuario, String profesion, String tipoTerapia, BigDecimal valorPorTerapia) {
        this.id = id;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.profesion = profesion;
        this.tipoTerapia = tipoTerapia;
        this.valorPorTerapia = valorPorTerapia;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getTipoTerapia() {
        return tipoTerapia;
    }

    public void setTipoTerapia(String tipoTerapia) {
        this.tipoTerapia = tipoTerapia;
    }

    public BigDecimal getValorPorTerapia() {
        return valorPorTerapia;
    }

    public void setValorPorTerapia(BigDecimal valorPorTerapia) {
        this.valorPorTerapia = valorPorTerapia;
    }
}


