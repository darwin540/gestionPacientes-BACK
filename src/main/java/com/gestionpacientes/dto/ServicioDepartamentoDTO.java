package com.gestionpacientes.dto;

import jakarta.validation.constraints.NotBlank;

public class ServicioDepartamentoDTO {

    private Long id;

    @NotBlank(message = "La abreviación es obligatoria")
    private String abreviacion;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private Boolean activo = true;

    // Constructores
    public ServicioDepartamentoDTO() {
    }

    public ServicioDepartamentoDTO(Long id, String abreviacion, String nombre, Boolean activo) {
        this.id = id;
        this.abreviacion = abreviacion;
        this.nombre = nombre;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}



