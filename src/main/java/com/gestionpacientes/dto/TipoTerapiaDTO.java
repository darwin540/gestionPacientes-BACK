package com.gestionpacientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class TipoTerapiaDTO {
    private Long id;

    @NotBlank(message = "El nombre del tipo de terapia es obligatorio")
    private String nombre;

    @NotNull(message = "El valor unitario es obligatorio")
    @PositiveOrZero(message = "El valor unitario debe ser positivo o cero")
    private BigDecimal valorUnitario;

    private Boolean activo = true;

    // Constructores
    public TipoTerapiaDTO() {
    }

    public TipoTerapiaDTO(Long id, String nombre, BigDecimal valorUnitario, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.valorUnitario = valorUnitario;
        this.activo = activo;
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

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}

